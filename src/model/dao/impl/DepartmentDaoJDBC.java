package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		
		PreparedStatement ps = null;
		
		try
		{
			ps = conn.prepareStatement("INSERT INTO department"
					+ " (Name)"
					+ " VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, obj.getName());
			
			int rows = ps.executeUpdate();
			
			if (rows > 0)
			{
				ResultSet rs = ps.getGeneratedKeys();
				
				if (rs.next())
				{
					int id = rs.getInt(1);
					obj.setId(id);
				}
				
				DB.closeResultSet(rs);
			}
			else
			{
				throw new DbException("Unexpected error! No rows affected!");
			}
					
		}
		catch (SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(ps);
		}

	}

	@Override
	public void update(Department obj) {

		PreparedStatement ps = null;
		
		try
		{
			ps = conn.prepareStatement("UPDATE department"
					+ " SET Name = ?"
					+ " WHERE Id = ?");
			
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());
			
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(ps);
		}

	}

	@Override
	public void deleteById(Integer id) {

		PreparedStatement ps = null;
		
		try
		{
			ps = conn.prepareStatement("DELETE FROM department"
					+ " WHERE Id = ?");
			
			ps.setInt(1, id);
			
			int rows = ps.executeUpdate();
			
			if (rows == 0)
			{
				throw new DbException("Error! There is no department with the inserted ID.");
			}
			
		}
		catch (SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(ps);
		}
	}

	@Override
	public Department findById(Integer id) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			ps = conn.prepareStatement("SELECT * FROM department"
					+ " WHERE Id = ?");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if (rs.next())
			{
				Department obj = new Department(rs.getInt("Id"), rs.getString("Name"));
				
				return obj;
			}
			
			return null;
		}
		catch (SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			List<Department> dep = new ArrayList<>();

			ps = conn.prepareStatement("SELECT * FROM department");
			rs = ps.executeQuery();

			while (rs.next()) {
				dep.add(new Department(rs.getInt("Id"), rs.getString("Name")));
			}

			return dep;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
}

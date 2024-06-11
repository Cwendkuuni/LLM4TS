
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    @Test
    public void testGetConnection() throws SQLException{
      SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

      Connection con = dataSource.getConnection();
      assertNotNull(con);

      dataSource.close();
    }

    @Test
    public void testGetConnectionWithCredentials() throws SQLException {
      String userName = "testUser";
      String password = "testPassword";
      SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
      dataSource.setUsername(userName);
      dataSource.setPassword(password);

      Connection conn = null;
      try {
          conn = dataSource.getConnection(userName, password);
      } catch (SQLException e) {
          fail("Exception should not have been thrown.");
      }
      assertNotNull(conn);

      dataSource.close();
    }
    
    @Test
    public void testResetConnection() throws SQLException{
      SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

      Connection con = dataSource.getConnection();
      assertNotNull(con);

      dataSource.resetConnection();

      try {
          con.isValid(1000);
          fail("Connection should be closed.");
      } catch (SQLException e) {
          // Expected
      }
      
      dataSource.close();
    }

    @Test
    public void testAddInitializer() throws SQLException {
      SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

      ConnectionInitializer initializer = connection -> {
          connection.setReadOnly(true);
      };
      dataSource.addInitializer(initializer);

      Connection connection = dataSource.getConnection();
      assertTrue(connection.isReadOnly());
      
      dataSource.close();
    }

    @Test
    public void testClose() throws SQLException{
      SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

      Connection con = dataSource.getConnection();
      assertNotNull(con);

      dataSource.close();

      try {
          con.isValid(1000);
          fail("Connection should be closed.");
      } catch (SQLException e) {
          // Expected
      }
    }
}

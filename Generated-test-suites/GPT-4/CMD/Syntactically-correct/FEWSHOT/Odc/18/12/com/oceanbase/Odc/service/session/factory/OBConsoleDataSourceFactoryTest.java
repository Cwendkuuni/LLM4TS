
package com.oceanbase.odc.service.session.factory;
 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

  private OBConsoleDataSourceFactory dataSourceFactory;
  private ConnectionConfig connectionConfig;

  @Before
  public void setUp() {

      // Initialize your connection config

      connectionConfig = new ConnectionConfig();

      // Add necessary configurations to connectionConfig

      boolean autoCommit = true;

      this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
  }

  @Test
  public void getUsername_ShouldReturnCorrectFormattedUsername() {
      String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
      assertNotNull(username);
      // Validate your username, according to your requirements
  }

  @Test
  public void getPassword_ShouldReturnSavedPassword() {
      String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
      assertNotNull(password);
      // Assert your saved password, according to your requirements
  }

  @Test
  public void resetUsername_ShouldResetUsername() {
      dataSourceFactory.resetUsername(value -> "newUsername");
      assertEquals("newUsername", dataSourceFactory.getUsername());
  }

  @Test
  public void resetPassword_ShouldResetPassword() {
      dataSourceFactory.resetPassword(value -> "newPassword");
      assertEquals("newPassword", dataSourceFactory.getPassword());
  }

  @Test
  public void getJdbcParams_ShouldReturnSavedParameters() {
      Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
      assertNotNull(params);
      //Assert your saved parameters, according to your requirements
  }

  @Test
  public void getJdbcUrl_ShouldReturnCorrectJdbcUrl() {
      String jdbcUrl = dataSourceFactory.getJdbcUrl();
      assertNotNull(jdbcUrl);
      //Assert your jdbcUrl, according to your requirements
  }

  @Test
  public void getDataSource_ShouldReturnDataSource() {
      DataSource dataSource = dataSourceFactory.getDataSource();
      assertNotNull(dataSource);
  }

}

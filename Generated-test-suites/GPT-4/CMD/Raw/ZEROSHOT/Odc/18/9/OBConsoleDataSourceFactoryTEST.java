
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class OBConsoleDataSourceFactoryTest {
  
  private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
  private ConnectionConfig connectionConfig;

  @Before
  public void setup() {
      connectionConfig = new ConnectionConfig();
      connectionConfig.setHost("localhost");
      connectionConfig.setPort(1234);
      connectionConfig.setUsername("username");
      connectionConfig.setPassword("password");
      connectionConfig.setDefaultSchema("defaultSchema");

      obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
  }
  
  @Test
  public void getJdbcUrlTest() {
    String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
    assertNotNull(jdbcUrl);
  }

  @Test
  public void getUsernameTest() {
    String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
    assertNotNull(username);
    assertEquals("username", username);
  }

  @Test
  public void getPasswordTest() {
    String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
    assertNotNull(password);
    assertEquals("password", password);
  }

  @Test
  public void getJdbcParamsTest() {
    Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
    assertNotNull(params);
  }

  @Test
  public void getDataSourceTest() {
    assertNotNull(obConsoleDataSourceFactory.getDataSource());
  }
  
  @Test
  public void getDialectTypeTest() {
    assertNotNull(obConsoleDataSourceFactory.getDialectType());
  }

  @Test
  public void deepCopyTest() {
    CloneableDataSourceFactory copy = obConsoleDataSourceFactory.deepCopy();
    assertNotNull(copy);
  }
  
  @Test
  public void getUsernameFromMethodTest() {
    String username = obConsoleDataSourceFactory.getUsername();
    assertNotNull(username);
    assertEquals("username", username);
  }

  @Test
  public void getPasswordFromMethodTest() {
    String password = obConsoleDataSourceFactory.getPassword();
    assertNotNull(password);
    assertEquals("password", password);
  }

  @Test
  public void getConnectTypeTest() {
    assertNotNull(obConsoleDataSourceFactory.getConnectType());
  }

  @Test
  public void getDefaultSchemaTest() {
    String schema = OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig);
    assertNotNull(schema);
    assertEquals("defaultSchema", schema);
  }
  
  @Test
  public void resetUsernameTest() {
    obConsoleDataSourceFactory.resetUsername(value -> "newUsername");
    assertEquals("newUsername", obConsoleDataSourceFactory.getUsername());
  }

  @Test
  public void resetPasswordTest() {
    obConsoleDataSourceFactory.resetPassword(value -> "newPassword");
    assertEquals("newPassword", obConsoleDataSourceFactory.getPassword());
  }

  @Test
  public void resetHostTest() {
    obConsoleDataSourceFactory.resetHost(value -> "newHost");
    assertEquals("newHost", ReflectionTestUtils.getField(obConsoleDataSourceFactory, "host"));
  }

  @Test
  public void resetPortTest() {
    obConsoleDataSourceFactory.resetPort(value -> 4321);
    assertEquals(4321, ReflectionTestUtils.getField(obConsoleDataSourceFactory, "port"));
  }

  @Test
  public void resetSchemaTest() {
    obConsoleDataSourceFactory.resetSchema(value -> "newSchema");
    assertEquals("newSchema", ReflectionTestUtils.getField(obConsoleDataSourceFactory, "defaultSchema"));
  }

  @Test
  public void resetParametersTest() {
    obConsoleDataSourceFactory.resetParameters(value -> null);
    assertNull(ReflectionTestUtils.getField(obConsoleDataSourceFactory, "parameters"));
  }
}

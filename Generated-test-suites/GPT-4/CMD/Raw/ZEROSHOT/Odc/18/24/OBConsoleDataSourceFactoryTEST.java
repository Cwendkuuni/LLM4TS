
package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class OBConsoleDataSourceFactoryTest {
  private ConnectionConfig mockConfig;
  private OBConsoleDataSourceFactory factory;

  @Before
  public void setup() {
    this.mockConfig = Mockito.mock(ConnectionConfig.class);
    Mockito.when(mockConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
    Mockito.when(mockConfig.getUsername()).thenReturn("testuser");
    Mockito.when(mockConfig.getPassword()).thenReturn("testpass");
    Mockito.when(mockConfig.getHost()).thenReturn("localhost");
    Mockito.when(mockConfig.getPort()).thenReturn(12345);
    Mockito.when(mockConfig.getDefaultSchema()).thenReturn("testschema");
    Mockito.when(mockConfig.getSslConfig()).thenReturn(null);
    Mockito.when(mockConfig.getSslFileEntry()).thenReturn(null);
    Mockito.when(mockConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
    this.factory = new OBConsoleDataSourceFactory(mockConfig, true, true);
  }

  @Test
  public void testGetUsername() throws Exception {
    String username = factory.getUsername();
    Assert.assertEquals("Username should be correct", "testuser", username);
  }

  @Test
  public void testGetPassword() throws Exception {
    String password = factory.getPassword();
    Assert.assertEquals("Password should be correct", "testpass", password);
  }

  @Test
  public void testGetJdbcUrl() throws Exception {
    String url = factory.getJdbcUrl();
    Assert.assertTrue("URL should starts with jdbc:", url.startsWith("jdbc:"));
  }

  @Test
  public void testGetDataSource() throws Exception {
    DataSource dataSource = factory.getDataSource();
    Assert.assertNotNull("DataSource should not be null", dataSource);
  }

  @Test
  public void testGetDialectType() throws Exception {
    DialectType dialectType = factory.getDialectType();
    Assert.assertEquals("DialectType should be OB_MYSQL", DialectType.OB_MYSQL, dialectType);
  }

  @Test
  public void testGetUsernameFunction() throws Exception {
    Method method = ReflectionUtils.findMethod(OBConsoleDataSourceFactory.class, "getUsername", ConnectionConfig.class);
    ReflectionUtils.makeAccessible(method);
    String username = (String)method.invoke(null, mockConfig);
    Assert.assertEquals("Username should be correct", "testuser", username);
  }

  @Test
  public void testGetPasswordFunction() throws Exception {
    Method method = ReflectionUtils.findMethod(OBConsoleDataSourceFactory.class, "getPassword", ConnectionConfig.class);
    ReflectionUtils.makeAccessible(method);
    String password = (String)method.invoke(null, mockConfig);
    Assert.assertEquals("Password should be correct", "testpass", password);
  }

  @Test
  public void testGetJdbcParams() throws Exception {
    Method method = ReflectionUtils.findMethod(OBConsoleDataSourceFactory.class, "getJdbcParams", ConnectionConfig.class);
    ReflectionUtils.makeAccessible(method);
    Map<String, String> params = (Map<String, String>)method.invoke(null, mockConfig);
    Assert.assertEquals("allowMultiQueries should be true", "true", params.get("allowMultiQueries"));
  }
}

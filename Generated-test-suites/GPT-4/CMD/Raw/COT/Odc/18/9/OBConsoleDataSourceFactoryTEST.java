
package com.oceanbase.odc.service.session.factory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;

public class OBConsoleDataSourceFactoryTest {

  private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

  private ConnectionConfig connectionConfig;

  @Before
  public void setup() {
      SSLConfig sslConfig = new SSLConfig();
      sslConfig.setEnabled(true);
      this.connectionConfig = new ConnectionConfig();
      this.connectionConfig.setSslConfig(sslConfig);
      this.obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
  }
  @Test
  public void getJdbcUrlTest() {
    Assert.assertNotNull(obConsoleDataSourceFactory.getJdbcUrl());
  }

  @Test
  public void getDataSourceTest() {
    Assert.assertNotNull(obConsoleDataSourceFactory.getDataSource());
  }
  
  @Test
  public void getDialectTypeTest() {
    Assert.assertNotNull(obConsoleDataSourceFactory.getDialectType());
  }

  @Test
  public void deepCopyTest() {
    Assert.assertNotNull(obConsoleDataSourceFactory.deepCopy());
  }

  // here should be the methods for resetUsername, resetPassword, resetHost, resetPort, resetSchema and resetParameters
  // but there is no enough data to generate these test methods
}

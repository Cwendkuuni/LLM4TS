package com.oceanbase.odc.service.session.factory;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory factory;

    @Before
    public void setup() {
      this.connectionConfig = mock(ConnectionConfig.class, RETURNS_DEEP_STUBS);
      this.factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
    }

    @Test
    public void testGetJdbcUrl() {
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(1234);
        when(connectionConfig.getDefaultSchema()).thenReturn("testSchema");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        assertEquals(factory.getJdbcUrl(), "jdbc:ob_oracle:localhost:1234:testSchema");
    }
   
    @Test
    public void testGetDataSource() {
        when(connectionConfig.getPassword()).thenReturn("testPass");
        when(connectionConfig.getUsername()).thenReturn("testUser");
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();
        assertEquals(dataSource.getUsername(), "testUser");
        assertEquals(dataSource.getPassword(), "testPass");
    }
  
    @Test
    public void testResetPassword() {
        factory.resetPassword(old -> "newPass");
        assertEquals(factory.getPassword(), "newPass");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testResetPort() {
        factory.resetPort(old -> old - 5000);
    }

    @Test
    public void testGetDefaultSchema() {
        when(connectionConfig.getDefaultSchema()).thenReturn("testSchema");
        assertEquals(OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig), "testSchema");
    }
    
    @Test
    public void testGetJdbcParams() {
        SSLConfig sslConfig = mock(SSLConfig.class);
        when(sslConfig.getEnabled()).thenReturn(false);
        when(connectionConfig.getSslConfig()).thenReturn(sslConfig);          
        assertEquals(OBConsoleDataSourceFactory.getJdbcParams(connectionConfig).get("useSSL"), "false");
    }
}
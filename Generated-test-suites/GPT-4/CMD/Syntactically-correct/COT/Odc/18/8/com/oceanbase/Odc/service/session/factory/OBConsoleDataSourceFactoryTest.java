package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.Test;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {

    private static final String HOST = "localhost";
    private static final Integer PORT = 3306;
    private static final String USER = "root";
    private static final String PASSWORD = "password";
  
    private ConnectionConfig mockConnectionConfig() {
        ConnectionConfig mock = mock(ConnectionConfig.class);
        mock.setHost(HOST);
        mock.setPort(PORT);
        mock.setUsername(USER);
        mock.setPassword(PASSWORD);
        mock.setDialectType(DialectType.MYSQL);
        return mock;
    }

    @Test
    public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mockConnectionConfig(), true);
        assertNotNull(factory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig config = mockConnectionConfig();
        assertEquals(OBConsoleDataSourceFactory.getUsername(config), "root");
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig config = mockConnectionConfig();
        assertEquals(OBConsoleDataSourceFactory.getPassword(config), "password");
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig config = mockConnectionConfig();
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(config);
        assertNotNull(params);
        assertEquals(params.get("maxAllowedPacket"), "64000000");
        assertEquals(params.get("allowMultiQueries"), "true");
        assertEquals(params.get("connectTimeout"), "5000");
        assertEquals(params.get("useSSL"), "false");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPort_ShouldThrowIllegalArgumentException_WhenNewPortIsNegative() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mockConnectionConfig(), true);
        factory.resetPort(value -> -1);
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory factory1 = new OBConsoleDataSourceFactory(mockConnectionConfig(), true);
        OBConsoleDataSourceFactory factory2 = factory1.deepCopy();
        assertNotNull(factory2);
        assertEquals(factory1.getJdbcUrl(), factory2.getJdbcUrl());
        assertEquals(factory1.getUsername(), factory2.getUsername());
        assertEquals(factory1.getPassword(), factory2.getPassword());
    }

    @Test
    public void testGetDataSource(){
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mockConnectionConfig(), true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType(){
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mockConnectionConfig(), true);
        assertEquals(factory.getDialectType(), DialectType.MYSQL);
    }

    @Test
    public void testResetUsername(){
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mockConnectionConfig(), true);
        factory.resetUsername(value -> "newUsername");
        assertEquals(factory.getUsername(), "newUsername");
    }

    @Test
    public void testResetPassword(){
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mockConnectionConfig(), true);
        factory.resetPassword(value -> "newPassword");
        assertEquals(factory.getPassword(), "newPassword");
    }

    @Test
    public void testResetHost(){
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mockConnectionConfig(), true);
        factory.resetHost(value -> "newHost");
        assertEquals(factory.getHost(), "newHost");
    }

    @Test
    public void testResetParameters(){
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(mockConnectionConfig(), true);
        Map<String, String> newParams = new HashMap<>();
        newParams.put("newParam","newValue");
        factory.resetParameters(value -> newParams);
        assertEquals(factory.getJdbcParams(), newParams);
    }
}

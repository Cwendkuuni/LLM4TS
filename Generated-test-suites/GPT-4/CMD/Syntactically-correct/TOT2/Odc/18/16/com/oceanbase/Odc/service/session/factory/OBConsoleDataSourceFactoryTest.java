package com.oceanbase.odc.service.session.factory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class OBConsoleDataSourceFactoryTest {
    @Test
    public void testGetUsername() {
        ConnectionConfig mockConnectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(mockConnectionConfig, true);
        String username = obConsoleDataSourceFactory.getUsername();
        assertNotNull(username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig mockConnectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(mockConnectionConfig, true);
        String password = obConsoleDataSourceFactory.getPassword();
        assertNotNull(password);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPort() {
        ConnectionConfig mockConnectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(mockConnectionConfig, true);
        CloneableDataSourceFactory.ValueMapper<Integer> valueMapper = value -> -1;
        obConsoleDataSourceFactory.resetPort(valueMapper);
    }

    // Other test cases...
}
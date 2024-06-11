package com.oceanbase.odc.service.session.factory;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {
    
    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        String jdbcUrl = dataSourceFactory.getJdbcUrl();

        assertNotNull("JdbcUrl should not be null", jdbcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPort_NegativePort() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory dataSourceFactory = 
                new OBConsoleDataSourceFactory(connectionConfig, true);

        CloneableDataSourceFactory.ValueMapper<Integer> mapper = 
                Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.when(mapper.map(Mockito.anyInt())).thenReturn(-5);

        dataSourceFactory.resetPort(mapper); 
    }

    // Additional test cases go here.
}

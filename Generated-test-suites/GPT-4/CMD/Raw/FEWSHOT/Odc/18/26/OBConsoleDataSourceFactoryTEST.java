package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    @Test
    public void testGetJdbcUrl() {
        ConnectionExtensionPoint mockConnectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        when(mockConnectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
                .thenReturn("jdbc:mysql://localhost:3306/myDb");
        OBConsoleDataSourceFactory dsFactory = createFactoryWithMockedConnectionExtensionPoint(mockConnectionExtensionPoint);

        assertEquals("jdbc:mysql://localhost:3306/myDb", dsFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        SingleConnectionDataSource mockDataSource = mock(SingleConnectionDataSource.class);
        doNothing().when(mockDataSource).addInitializer(any());
        ConnectionExtensionPoint mockConnectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        when(mockConnectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
                .thenReturn("jdbc:mysql://localhost:3306/myDb");
        when(mockConnectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        OBConsoleDataSourceFactory dsFactory = createFactoryWithMockedConnectionExtensionPoint(mockConnectionExtensionPoint);

        DataSource dataSource = dsFactory.getDataSource();

        assertNull(dataSource);
        verify(mockConnectionExtensionPoint, times(1)).getDriverClassName();
    }

    //Create similar methods for testing other methods of OBConsoleDataSourceFactory

    private OBConsoleDataSourceFactory createFactoryWithMockedConnectionExtensionPoint(
            ConnectionExtensionPoint connectionExtensionPoint) {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("host");
        connectionConfig.setPort(3306);
        return new OBConsoleDataSourceFactory(connectionConfig, true, true) {
            @Override
            public ConnectionExtensionPoint getConnectionExtension() {
                return connectionExtensionPoint;
            }

            @Override
            protected SingleConnectionDataSource createDataSource() {
                return mockDataSource;
            }
        };
    }
}
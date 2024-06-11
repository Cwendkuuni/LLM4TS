package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;
    private List<ConnectionInitializer> initializerList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        connectionMock = Mockito.mock(Connection.class);
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisherMock);
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                // initialization logic
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws Exception {
        Mockito.when(connectionMock.isValid(Mockito.anyInt())).thenReturn(true);
        Mockito.when(connectionMock.isClosed()).thenReturn(false);
        dataSource.connection = connectionMock;
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(dataSource.getLoginTimeout()));
        assertFalse(connection.isClosed());

        Mockito.verify(connectionMock).isValid(Mockito.anyInt());
        Mockito.verify(connectionMock, Mockito.never()).close();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws Exception {
        Mockito.when(connectionMock.isValid(Mockito.anyInt())).thenReturn(false);
        dataSource.connection = connectionMock;
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionNullConnection() throws Exception {
        dataSource.connection = null;
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidConnection() throws Exception {
        Mockito.when(connectionMock.isValid(Mockito.anyInt())).thenThrow(new SQLException());
        dataSource.connection = connectionMock;
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        Connection connectionProxy = dataSource.getConnectionProxy(connectionMock);
        assertNotNull(connectionProxy);
        assertEquals(connectionMock, connectionProxy);
    }

    @Test
    public void testResetConnection() throws Exception {
        dataSource.resetConnection();
        assertNull(dataSource.connection);
        Mockito.verify(connectionMock).close();
        Mockito.verify(eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testResetConnectionNullConnection() throws Exception {
        dataSource.resetConnection();
        assertNull(dataSource.connection);
        Mockito.verify(connectionMock, Mockito.never()).close();
        Mockito.verify(eventPublisherMock, Mockito.never()).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer1 = Mockito.mock(ConnectionInitializer.class);
        ConnectionInitializer initializer2 = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer1);
        dataSource.addInitializer(initializer2);
        assertEquals(2, dataSource.initializerList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInitializerNullInitializer() {
        dataSource.addInitializer(null);
    }
}
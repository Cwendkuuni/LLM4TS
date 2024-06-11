package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisherMock;
    private Connection connectionMock;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        connectionMock = Mockito.mock(Connection.class);
        dataSource.setEventPublisher(eventPublisherMock);
    }

    @Test
    public void testGetConnection() throws SQLException {
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                // no-op
            }
        });

        Mockito.when(connectionMock.isValid(dataSource.getLoginTimeout())).thenReturn(true);
        Mockito.when(connectionMock.isClosed()).thenReturn(false);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(dataSource.getLoginTimeout()));
        assertFalse(connection.isClosed());

        Mockito.verify(eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnectionWhenConnectionIsInvalid() throws SQLException {
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                Mockito.when(connection.isValid(dataSource.getLoginTimeout())).thenReturn(false);
            }
        });

        Mockito.when(connectionMock.isValid(dataSource.getLoginTimeout())).thenReturn(false);
        Mockito.when(connectionMock.isClosed()).thenReturn(false);

        try {
            dataSource.getConnection();
            assertFalse(true);
        } catch (SQLException e) {
            // expected
        }

        Mockito.verify(eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnectionWhenConnectionIsClosed() throws SQLException {
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                Mockito.when(connection.isValid(dataSource.getLoginTimeout())).thenReturn(true);
                Mockito.when(connection.isClosed()).thenReturn(false);
            }
        });

        Mockito.when(connectionMock.isValid(dataSource.getLoginTimeout())).thenReturn(true);
        Mockito.when(connectionMock.isClosed()).thenReturn(true);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(dataSource.getLoginTimeout()));
        assertTrue(connection.isClosed());

        Mockito.verify(eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnectionWhenAutoReconnectIsFalse() throws SQLException {
        dataSource = new SingleConnectionDataSource();

        Mockito.when(connectionMock.isValid(dataSource.getLoginTimeout())).thenReturn(false);
        Mockito.when(connectionMock.isClosed()).thenReturn(false);

        try {
            dataSource.getConnection();
            assertFalse(true);
        } catch (SQLException e) {
            // expected
        }

        Mockito.verify(eventPublisherMock, Mockito.never()).publishEvent(Mockito.any());
    }

    @Test
    public void testAddInitializer() {
        List<ConnectionInitializer> initializers = new ArrayList<>();

        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                initializers.add(Mockito.mock(ConnectionInitializer.class));
            }
        });

        assertEquals(1, initializers.size());
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                Mockito.when(connection.isValid(dataSource.getLoginTimeout())).thenReturn(true);
            }
        });

        Mockito.when(connectionMock.isValid(dataSource.getLoginTimeout())).thenReturn(true);

        dataSource.resetConnection();

        Mockito.verify(eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testResetConnectionWhenNoConnection() throws SQLException {
        dataSource.resetConnection();

        Mockito.verify(eventPublisherMock, Mockito.never()).publishEvent(Mockito.any());
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionWhenDriverFailsToCreateConnection() throws SQLException {
        DataSource datasource = Mockito.mock(DataSource.class);
        Mockito.when(datasource.getUsername()).thenReturn("test");
        Mockito.when(datasource.getPassword()).thenReturn("test");
        Mockito.when(datasource.getLoginTimeout()).thenReturn(1000);

        dataSource = new SingleConnectionDataSource();
        dataSource.setDataSource(datasource);

        Mockito.when(DriverManager.getConnection(Mockito.anyString(), Mockito.any(), Mockito.anyInt()))
                .thenThrow(new SQLException("Test error"));

        try {
            dataSource.innerCreateConnection();
            assertFalse(true);
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("Test error"));
        }

        Mockito.verify(eventPublisherMock, Mockito.never()).publishEvent(Mockito.any());
    }

    @After
    public void tearDown() {
        Mockito.reset(eventPublisherMock, connectionMock);
        dataSource = null;
    }
}
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisherMock;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() {
        initializerList = new ArrayList<>();
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        connectionMock = Mockito.mock(Connection.class);
        dataSource = new SingleConnectionDataSource() {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return connectionMock;
            }

            @Override
            public void initConnection(Connection connection) throws SQLException {
                super.initConnection(connection);
                for (ConnectionInitializer initializer : initializerList) {
                    try {
                        initializer.init(connection);
                    } catch (Exception e) {
                        fail("Initializer failed");
                    }
                }
            }
        };
        dataSource.addInitializer(mock(ConnectionInitializer.class));
        dataSource.setEventPublisher(eventPublisherMock);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connectionMock.isValid(anyInt())).thenReturn(true);
        when(connectionMock.isClosed()).thenReturn(false);

        Connection connection = dataSource.getConnection();
        assertEquals(connectionMock, connection);

        verify(connectionMock, times(1)).isValid(anyInt());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionConnectionIsClosed() throws SQLException {
        when(connectionMock.isValid(anyInt())).thenReturn(false);
        when(connectionMock.isClosed()).thenReturn(true);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidUsernameOrPassword() throws SQLException {
        dataSource.setUsername("wrongUsername");
        dataSource.setPassword("wrongPassword");

        try {
            dataSource.getConnection();
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();

        verify(connectionMock, times(1)).close();
        verify(connectionMock, times(1)).close();
        verify(eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test(expected = ConflictException.class)
    public void testResetConnectionConnectionIsOccupied() throws SQLException {
        dataSource.resetConnection();
        dataSource.resetConnection();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializerMock = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializerMock);
        assertTrue(dataSource.initializerList.contains(initializerMock));
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.close();

        verify(connectionMock, times(1)).close();
    }

    @Test
    public void testCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertEquals(connectionMock, connection);

        verify(connectionMock, times(1)).createStatement();
    }

    @After
    public void tearDown() throws Exception {
        DriverManager.getConnection("jdbc:none"); // Clear JDBC driver cache
    }
}
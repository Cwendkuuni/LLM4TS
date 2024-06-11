
package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {
    
    @Mock
    Connection connectionMock;

    @Mock
    EventPublisher eventPublisherMock;

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setup() throws SQLException {
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setEventPublisher(eventPublisherMock);

        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.isValid(any(Integer.class))).thenReturn(true);
        singleConnectionDataSource.connection = connectionMock;
    }

    @Test
    public void testGetConnection_whenConnectionIsValid_returnsProxyConnection() throws SQLException {
        Connection conn = singleConnectionDataSource.getConnection();
        assertNotEquals(conn, connectionMock);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_whenConnectionIsNotValid_thenThrowSQLException() throws SQLException {
        when(connectionMock.isClosed()).thenReturn(true);
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testGetConnection_givenUsernameAndPassword_returnsProxyConnection() throws SQLException {
        singleConnectionDataSource.setUsername("username");
        singleConnectionDataSource.setPassword("password");
        Connection conn = singleConnectionDataSource.getConnection("username", "password");
        assertNotEquals(conn, connectionMock);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_givenWrongUsernameAndPassword_thenThrowSQLException() throws SQLException {
        singleConnectionDataSource.setUsername("username");
        singleConnectionDataSource.setPassword("password");
        singleConnectionDataSource.getConnection("wrong_user", "wrong_password");
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        verify(eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @After
    public void tearDown() {
        singleConnectionDataSource.close();
    }
}

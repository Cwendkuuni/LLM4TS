package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SingleConnectionDataSourceTest {

    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final boolean AUTO_COMMIT = true;
    private static final boolean AUTO_RECONNECT = true;
    private static final long TIME_OUT_MILLIS = 10 * 1000;
    private static final int LOGIN_TIMEOUT = 5;

    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisherMock;
    private Connection connectionMock;

    @Before
    public void setUp() {
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        connectionMock = Mockito.mock(Connection.class);
        Mockito.when(connectionMock.isClosed()).thenReturn(false);
        Mockito.when(connectionMock.isValid(LOGIN_TIMEOUT)).thenReturn(true);
        dataSource = new SingleConnectionDataSource(AUTO_RECONNECT);
        dataSource.setAutoCommit(AUTO_COMMIT);
        dataSource.setEventPublisher(eventPublisherMock);
    }

    @After
    public void tearDown() throws SQLException {
        dataSource.close();
    }

    @Test
    public void testGetConnectionSuccess() throws SQLException {
        Mockito.when(connectionMock.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));

        dataSource.addInitializer(connection -> {});
        Connection connection = dataSource.getConnection();
        Assert.assertNotNull(connection);
        Mockito.verify(eventPublisherMock, Mockito.times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws SQLException {
        dataSource.addInitializer(connection -> {});
        dataSource.getConnection("InvalidUsername", "InvalidPassword");
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionClosedConnection() throws SQLException {
        dataSource.addInitializer(connection -> {
            Mockito.when(connection.isClosed()).thenReturn(true);
        });
        dataSource.getConnection();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionTimeout() throws SQLException {
        dataSource.addInitializer(connection -> {
            Mockito.when(connection.isValid(LOGIN_TIMEOUT)).thenReturn(false);
        });
        dataSource.getConnection();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionOccupied() throws SQLException {
        dataSource.addInitializer(connection -> {
            dataSource.setTimeOutMillis(1);
        });
        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Mockito.verify(eventPublisherMock, Mockito.times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        List<ConnectionInitializer> initializerList = new LinkedList<>();
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                initializerList.add(connection);
            }
        });
        dataSource.getConnection();
        Assert.assertNotNull(initializerList);
        Assert.assertEquals(1, initializerList.size());
    }
}
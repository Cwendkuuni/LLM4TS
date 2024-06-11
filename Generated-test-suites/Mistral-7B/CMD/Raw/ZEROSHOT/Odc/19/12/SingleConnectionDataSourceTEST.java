package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    private SingleConnectionDataSource singleConnectionDataSource;
    private Connection connectionMock;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource(true);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        singleConnectionDataSource.setTimeOutMillis(10 * 1000);

        try {
            connectionMock = DriverManager.getConnection("jdbc:mock:database", "username", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        singleConnectionDataSource.addInitializer(connection -> {
            when(connection.isValid(anyLong())).thenReturn(true);
        });
    }

    @After
    public void tearDown() throws SQLException {
        singleConnectionDataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(eventPublisher.publishEvent(any(ConnectionResetEvent.class))).thenReturn(true);

        Connection connection = singleConnectionDataSource.getConnection();
        Assert.assertNotNull(connection);
        Assert.assertEquals(connectionMock, connection);

        Assert.assertTrue(singleConnectionDataSource.isConnectionValid());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        singleConnectionDataSource.setUsername("invalidUsername");
        singleConnectionDataSource.setPassword("invalidPassword");

        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.setConnection(connectionMock);

        singleConnectionDataSource.resetConnection();

        Assert.assertNull(singleConnectionDataSource.getConnection());
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(connection -> {});

        List<ConnectionInitializer> initializerList = singleConnectionDataSource.getInitializerList();
        Assert.assertEquals(1, initializerList.size());
    }

    @Test
    public void testSetAutoCommit() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);

        Connection connection = singleConnectionDataSource.getConnection();
        Assert.assertTrue(connection.getAutoCommit());
    }
}
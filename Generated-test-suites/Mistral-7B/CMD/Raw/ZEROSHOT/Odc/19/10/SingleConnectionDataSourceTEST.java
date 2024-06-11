package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerCallable;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisherTest;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializerTest;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2023-03-21 11:30
 */
public class SingleConnectionDataSourceTest {

    private Connection connection;
    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisher;
    private ConnectionInitializer connectionInitializer;

    @Before
    public void setUp() throws Exception {
        connection = mock(Connection.class);
        dataSource = new SingleConnectionDataSource();
        eventPublisher = mock(EventPublisher.class);
        connectionInitializer = mock(ConnectionInitializer.class);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.addInitializer(connectionInitializer);
    }

    @Test
    public void testGetConnection() throws Exception {
        doAnswer((Answer) invocationOnMock -> dataSource.connection = connection)
                .when(dataSource).innerCreateConnection();

        assertNull(dataSource.getConnection());

        dataSource.getConnection();

        assertNotNull(dataSource.connection);
        assertSame(connection, dataSource.getConnection());
    }

    @Test
    public void testTryLock() throws Exception {
        dataSource.lock = mock(Lock.class);
        assertTrue(dataSource.tryLock());

        doAnswer((AnswerCallable<Boolean, ?>) invocation -> false).when(dataSource.lock)
                .tryLock(anyLong(), any(TimeUnit.class));

        assertThrows(ConflictException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        dataSource.connection = connection;
        dataSource.lock = new ReentrantLock();

        Connection result = dataSource.getConnectionProxy(connection);

        assertSame(connection, result);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws Exception {
        String username = "testUser";
        String password = "testPassword";

        doAnswer((Answer) invocationOnMock -> connection)
                .when(dataSource).innerCreateConnection();

        assertThrows(SQLException.class, () -> dataSource.getConnection(null, null));

        assertSame(connection, dataSource.getConnection(username, password));
    }

    @Test
    public void testResetConnection() throws Exception {
        doAnswer((Answer) invocationOnMock -> dataSource.connection = connection)
                .when(dataSource).innerCreateConnection();

        dataSource.getConnection();
        assertNotNull(dataSource.connection);

        dataSource.resetConnection();
        assertNull(dataSource.connection);

        dataSource.getConnection();
        assertNotNull(dataSource.connection);
    }

    @Test
    public void testClose() throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:mock:test")) {
            dataSource.connection = connection;
            dataSource.lock = new ReentrantLock();
            dataSource.setAutoCommit(true);

            dataSource.getConnectionProxy(connection);

            dataSource.close();
            assertNull(dataSource.connection);
        }
    }

    @Test
    public void testPrepareConnection() throws Exception {
        doAnswer((Answer) invocationOnMock -> connection)
                .when(dataSource).innerCreateConnection();

        dataSource.prepareConnection(connection);
        assertSame(dataSource.getAutoCommit(), connection.getAutoCommit());
    }

    @Test
    public void testAddInitializer() throws Exception {
        doAnswer((Answer) invocationOnMock -> {
            connectionInitializer.init(connection);
            return null;
        }).when(connectionInitializer)
                .init(connection);

        dataSource.addInitializer(connectionInitializer);
        dataSource.getConnection();

        verify(connectionInitializer).init(connection);
    }

    @Test
    public void testInitializeConnection() throws Exception {
        Connection connection = mock(Connection.class);
        dataSource.connection = connection;

        doAnswer((Answer) invocationOnMock -> {
            connectionInitializer.init(connection);
            return null;
        }).when(connectionInitializer)
                .init(connection);

        dataSource.initConnection(connection);

        verify(connectionInitializer).init(connection);
    }

    @Test
    public void testOnConnectionReset() throws Exception {
        Connection connection = mock(Connection.class);

        doAnswer((Answer) invocationOnMock -> eventPublisher.publishEvent(any(ConnectionResetEvent.class)))
                .when(eventPublisher)
                .publishEvent(any(ConnectionResetEvent.class));

        dataSource.onConnectionReset(connection);

        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        doAnswer((Answer) invocationOnMock -> connection)
                .when(dataSource).newConnectionFromDriver(anyString(), anyString());

        Connection result = dataSource.innerCreateConnection();

        assertSame(connection, result);
    }

    @Test
    public void testSetAutoCommit() throws Exception {
        Connection connection = mock(Connection.class);
        dataSource.connection = connection;

        doAnswer((Answer) invocationOnMock -> {
            connection.setAutoCommit(true);
            return null;
        }).when(connection)
                .setAutoCommit(true);

        dataSource.setAutoCommit(true);

        verify(connection).setAutoCommit(true);
    }

    @Test
    public void testGetAutoCommit() throws Exception {
        Connection connection = mock(Connection.class);
        dataSource.connection = connection;

        doAnswer((Answer) invocationOnMock -> false)
                .when(connection)
                .getAutoCommit();

        assertFalse(dataSource.getAutoCommit());
    }

    @Test
    public void testSetEventPublisher() throws Exception {
        EventPublisher eventPublisher = mock(EventPublisher.class);

        dataSource.setEventPublisher(eventPublisher);

        assertSame(eventPublisher, dataSource.eventPublisher);
    }

    @Test
    public void testGetEventPublisher() throws Exception {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.eventPublisher = eventPublisher;

        assertSame(eventPublisher, dataSource.getEventPublisher());
    }

    @Test
    public void testSetAutoReconnect() throws Exception {
        dataSource.setAutoReconnect(true);

        assertTrue(dataSource.autoReconnect);
    }

    @Test
    public void testSetAutoReconnectWithoutArgument() throws Exception {
        dataSource.setAutoReconnect();

        assertFalse(dataSource.autoReconnect);
    }

    @Test
    public void testSetAutoReconnectWithFalse() throws Exception {
        dataSource.setAutoReconnect(false);

        assertFalse(dataSource.autoReconnect);
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

}
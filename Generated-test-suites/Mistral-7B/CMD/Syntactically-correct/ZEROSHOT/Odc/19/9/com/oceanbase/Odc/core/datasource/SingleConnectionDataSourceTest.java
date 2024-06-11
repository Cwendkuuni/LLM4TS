package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private ConnectionInitializer connectionInitializerMock;
    private EventPublisher eventPublisherMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        dataSource = new SingleConnectionDataSource();
        dataSource.setAutoReconnect(true);
        dataSource.setTimeOutMillis(1000);

        connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(connectionInitializerMock);

        eventPublisherMock = Mockito.mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisherMock);

        Mockito.doReturn(connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)).when(dataSource)
                .innerCreateConnection();
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull(dataSource.getConnection());
        assertSame(connection, dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        assertNotSame(connection, dataSource.getConnection("invalidUsername", "invalidPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection resetConnection = dataSource.getConnection();
        dataSource.resetConnection();
        assertNotSame(resetConnection, dataSource.getConnection());
        Mockito.verify(eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testResetConnectionWithoutAutoReconnect() throws SQLException {
        dataSource = new SingleConnectionDataSource(false);
        Connection resetConnection = dataSource.getConnection();
        dataSource.resetConnection();
        assertSame(resetConnection, dataSource.getConnection());
        Mockito.verifyNoInteractions(eventPublisherMock);
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(connectionInitializerMock);
        Mockito.verify(connectionInitializerMock).init(connection);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithConnectionOccupied() throws SQLException, InterruptedException {
        SingleConnectionDataSource anotherDataSource = new SingleConnectionDataSource();
        anotherDataSource.setLock(new ReentrantLock());
        anotherDataSource.innerCreateConnection();

        dataSource.setLock(anotherDataSource.getLock());
        try {
            dataSource.getConnection();
            fail();
        } catch (ConflictException e) {
            // expected
        }
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertSame(connection, connectionProxy);
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws SQLException {
        List<Boolean> isClosedCalled = new LinkedList<Boolean>();
        Connection closeIgnoreInvocationHandler = Mockito.mock(Connection.class);
        Mockito.doAnswer(invocation -> {
            isClosedCalled.add(true);
            return null;
        }).when(closeIgnoreInvocationHandler).close();

        Connection connectionProxy = dataSource.getConnectionProxy(closeIgnoreInvocationHandler);
        connectionProxy.close();
        assertTrue(isClosedCalled.size() > 0);
    }

    @Test
    public void testTryLock() throws InterruptedException, SQLException {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testTryLockWithTimeout() throws InterruptedException, SQLException {
        assertFalse(dataSource.tryLock(0, TimeUnit.MILLISECONDS));
    }
}
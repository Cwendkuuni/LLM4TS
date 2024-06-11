package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.test.mock.MockConnection;
import com.oceanbase.odc.core.datasource.test.mock.MockConnectionInitializer;

/**
 * Test class for {@link SingleConnectionDataSource}.
 *
 * @author yh263208
 */
public class SingleConnectionDataSourceTest {

    private static final long TIME_OUT_MILLIS = 10 * 1000;
    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;
    private Lock lock;

    @Before
    public void setup() throws Exception {
        eventPublisher = Mockito.mock(EventPublisher.class);
        dataSource = new SingleConnectionDataSource(true) {
            @Override
            public void onConnectionReset(Connection connection) {
                super.onConnectionReset(connection);
                Mockito.verify(eventPublisher).publishEvent(ArgumentMatchers.any(ConnectionResetEvent.class));
            }
        };
        dataSource.setEventPublisher(eventPublisher);
        dataSource.setTimeOutMillis(TIME_OUT_MILLIS);
        Mockito.doAnswer(invocation -> {
            connection = new MockConnection();
            return connection;
        }).when(dataSource).innerCreateConnection();
        Mockito.doAnswer(invocation -> {
            ConnectionInitializer connectionInitializer = (ConnectionInitializer) invocation.getArguments()[0];
            connectionInitializer.init(connection);
            return null;
        }).when(dataSource).initConnection(ArgumentMatchers.any(Connection.class));
        dataSource.addInitializer(new MockConnectionInitializer());
        dataSource.setAutoCommit(true);
        dataSource.setAutoReconnect(true);
        Mockito.doNothing().when(connection).close();
        lock = new ReentrantLock();
    }

    @After
    public void cleanup() throws Exception {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetConnection() throws Exception {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertEquals(dataSource.getConnection(), connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws Exception {
        dataSource.setUsername("test");
        dataSource.setPassword("test");
        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws Exception {
        Connection resetConnection = dataSource.getConnection();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
        assertNull(resetConnection);
        connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testAddInitializer() throws Exception {
        List<Object> initializerList = new LinkedList<>();
        dataSource.addInitializer(new MockConnectionInitializer() {
            @Override
            public void init(Connection connection) throws Exception {
                initializerList.add(connection);
            }
        });
        Connection connection = dataSource.getConnection();
        assertEquals(1, initializerList.size());
        assertEquals(connection, initializerList.get(0));
    }

    @Test
    public void testClose() throws Exception {
        dataSource.close();
        Connection connection = dataSource.getConnection();
        assertNull(connection);
    }

    @Test(expected = ConflictException.class)
    public void testTryLock() throws Exception {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setTimeOutMillis(TIME_OUT_MILLIS);
        dataSource.lock.lock();
        dataSource.lock.unlock();
        dataSource.lock.lock();
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertSame(connection, connectionProxy);
    }

    @Test
    public void testPrepareConnection() throws Exception {
        dataSource.setAutoCommit(false);
        dataSource.getConnection();
        dataSource.prepareConnection(connection);
        assertFalse(connection.getAutoCommit());
    }

    @Test
    public void testTryLockWithTimeout() throws Exception {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setTimeOutMillis(1);
        dataSource.lock.lock();
        try {
            dataSource.getConnection();
            fail();
        } catch (ConflictException ignore) {
            // expected
        }
    }

}

package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.oceanbase.odc.common.event.EventPublisher;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private Connection connection;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testAutoReconnect() {
        assertFalse(singleConnectionDataSource.getAutoReconnect());
    }

    @Test
    public void testAutoCommit() {
        singleConnectionDataSource.setAutoCommit(true);
        assertTrue(singleConnectionDataSource.getAutoCommit());
    }

    @Test
    public void testTimeOutMillis() {
        long timeout = 2000L;
        singleConnectionDataSource.setTimeOutMillis(timeout);
        assertEquals(timeout, singleConnectionDataSource.getTimeOutMillis());
    }

    @Test
    public void testConnectionMethods() throws Exception {
        singleConnectionDataSource.getConnection();
        singleConnectionDataSource.resetConnection();
        singleConnectionDataSource.addInitializer(Connection::clearWarnings);
        singleConnectionDataSource.getTimeOutMillis();
    }

    @Test
    public void testClose() throws Exception {
        singleConnectionDataSource.close();
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void tryLockTest() throws InterruptedException {
        singleConnectionDataSource.lock = new ReentrantLock();
        singleConnectionDataSource.timeOutMillis = 1;
        assertTrue(singleConnectionDataSource.tryLock());
    }
}

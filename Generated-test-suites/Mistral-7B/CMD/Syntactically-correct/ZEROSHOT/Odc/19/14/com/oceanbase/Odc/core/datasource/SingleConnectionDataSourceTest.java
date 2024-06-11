package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.common.event.EventPublisherImpl;

/**
 * Test class for SingleConnectionDataSource.
 *
 * @author yh263208
 * @date 2023-03-31 16:33
 */
public class SingleConnectionDataSourceTest {

    private static final String DUMMY_USERNAME = "dummy_username";
    private static final String DUMMY_PASSWORD = "dummy_password";

    private ConnectionInitializer connectionInitializer;
    private EventPublisher eventPublisher;
    private SingleConnectionDataSource singleConnectionDataSource;
    private Connection testConnection;

    @Before
    public void setup() {
        connectionInitializer = mock(ConnectionInitializer.class);
        eventPublisher = new EventPublisherImpl();
        singleConnectionDataSource = new SingleConnectionDataSource(true) {
            @Override
            protected Connection innerCreateConnection() throws SQLException {
                testConnection = newConnectionFromDriver(DUMMY_USERNAME, DUMMY_PASSWORD);
                return testConnection;
            }
        };
        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        assertSame(testConnection, connection);
    }

    @Test
    public void testGetConnection_WithInvalidUsernameOrPassword() throws SQLException {
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection("invalid_username", "invalid_password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection resetConnection = singleConnectionDataSource.resetConnection();
        assertNull(resetConnection);
        assertNull(singleConnectionDataSource.connection);
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(connectionInitializer);
        List<ConnectionInitializer> initializerList = (List<ConnectionInitializer>) singleConnectionDataSource.initializerList.clone();
        assertTrue(initializerList.contains(connectionInitializer));
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.close();
        assertNull(singleConnectionDataSource.connection);
    }

    @Test
    public void testGetAutoCommit() {
        assertNull(singleConnectionDataSource.autoCommit);
    }

    @Test
    public void testSetAutoCommit() {
        singleConnectionDataSource.setAutoCommit(true);
        assertTrue(singleConnectionDataSource.autoCommit);
    }

    @Test
    public void testGetAutoReconnect() {
        assertTrue(singleConnectionDataSource.autoReconnect);
    }

    @Test
    public void testSetAutoReconnect() {
        singleConnectionDataSource.setAutoReconnect(false);
        assertFalse(singleConnectionDataSource.autoReconnect);
    }

    @Test
    public void testGetTimeOutMillis() {
        assertEquals(10 * 1000, singleConnectionDataSource.timeOutMillis);
    }

    @Test
    public void testSetTimeOutMillis() {
        singleConnectionDataSource.setTimeOutMillis(5 * 1000);
        assertEquals(5 * 1000, singleConnectionDataSource.timeOutMillis);
    }

    @Test
    public void testEventPublisher_IsNotNull() {
        assertNotNull(singleConnectionDataSource.eventPublisher);
    }

    @Test
    public void testEventPublisher_PublishConnectionResetEvent() throws SQLException {
        when(connectionInitializer.init(testConnection)).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                ConnectionResetEvent connectionResetEvent = new ConnectionResetEvent(testConnection);
                verify(eventPublisher).publishEvent(connectionResetEvent);
                return null;
            }
        });
        singleConnectionDataSource.resetConnection();
    }

    @After
    public void teardown() throws SQLException {
        if (testConnection != null) {
            testConnection.close();
        }
    }
}
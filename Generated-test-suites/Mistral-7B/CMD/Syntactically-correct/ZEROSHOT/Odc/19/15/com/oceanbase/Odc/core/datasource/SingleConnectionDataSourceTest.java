package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.event.MockEventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Junit test file for SingleConnectionDataSource class
 *
 * @author yh263208
 * @date 2023-03-13 10:15
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Mock
    private MockEventPublisher mockEventPublisher;

    private Connection connection;

    /**
     * Set up mock objects and initialize test object
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        List<ConnectionInitializer> initializerList = new LinkedList<>();
        initializerList.add(connectionInitializer);
        singleConnectionDataSource.setInitializerList(initializerList);
        this.connection = Mockito.mock(Connection.class);
    }

    /**
     * Test getConnection method
     */
    @Test
    public void testGetConnection() throws SQLException {
        Mockito.when(connectionInitializer.init(connection)).thenReturn(null);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.setAutoCommit(Boolean.FALSE);
        Connection expectedConnection = singleConnectionDataSource.getConnection();
        Connection actualConnection = singleConnectionDataSource.getConnection();
        assertEquals(expectedConnection, actualConnection);
        assertFalse(expectedConnection.isClosed());
        Mockito.verify(connectionInitializer).init(ArgumentMatchers.eq(connection));
        Mockito.verify(eventPublisher, Mockito.never()).publishEvent(ArgumentMatchers.isA(ConnectionResetEvent.class));
    }

    /**
     * Test getConnection method with invalid username or password
     */
    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        singleConnectionDataSource.setUsername("testUsername");
        singleConnectionDataSource.setPassword("testPassword");
        singleConnectionDataSource.getConnection();
    }

    /**
     * Test resetConnection method
     */
    @Test
    public void testResetConnection() throws SQLException {
        Mockito.when(connectionInitializer.init(connection)).thenReturn(null);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.setEventPublisher(mockEventPublisher);
        singleConnectionDataSource.setAutoReconnect(Boolean.TRUE);
        singleConnectionDataSource.setTimeOutMillis(1000);

        singleConnectionDataSource.resetConnection();
        Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(ArgumentMatchers.isA(ConnectionResetEvent.class));
        Mockito.verify(connection).close();
    }

    /**
     * Test addInitializer method
     */
    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(connectionInitializer);
        List<ConnectionInitializer> initializerList = singleConnectionDataSource.getInitializerList();
        assertEquals(1, initializerList.size());
        assertTrue(initializerList.contains(connectionInitializer));
    }

    /**
     * Test close method
     */
    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.close();
        Connection expectedConnection = singleConnectionDataSource.getConnection();
        assertNull(expectedConnection);
    }

    /**
     * Test initConnection method
     */
    @Test
    public void testInitConnection() throws SQLException {
        Mockito.when(connectionInitializer.init(connection)).thenReturn(null);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.initConnection(connection);
        Mockito.verify(connectionInitializer, Mockito.times(1)).init(ArgumentMatchers.eq(connection));
    }

    /**
     * Test onConnectionReset method
     */
    @Test
    public void testOnConnectionReset() throws SQLException {
        singleConnectionDataSource.setEventPublisher(mockEventPublisher);
        singleConnectionDataSource.resetConnection();
        Mockito.verify(mockEventPublisher, Mockito.times(1)).publishEvent(ArgumentMatchers.isA(ConnectionResetEvent.class));
    }

    /**
     * Test innerCreateConnection method
     */
    @Test(expected = SQLException.class)
    public void testInnerCreateConnection() throws SQLException {
        Mockito.when(connectionInitializer.init(connection)).thenThrow(new SQLException("Test exception"));
        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.setEventPublisher(mockEventPublisher);
        singleConnectionDataSource.setAutoReconnect(Boolean.FALSE);
        singleConnectionDataSource.innerCreateConnection();
        Mockito.verify(mockEventPublisher, Mockito.times(1)).publishEvent(ArgumentMatchers.isA(ConnectionResetEvent.class));
    }

    /**
     * Test getConnectionProxy method
     */
    @Test
    public void testGetConnectionProxy() throws SQLException {
        singleConnectionDataSource.setLock(new ReentrantLock());
        singleConnectionDataSource.setConnection(connection);
        Connection expectedConnection = singleConnectionDataSource.getConnectionProxy(connection);
        Connection actualConnection = singleConnectionDataSource.getConnectionProxy(connection);
        assertEquals(expectedConnection, actualConnection);
        Mockito.verify(connection, Mockito.never()).close();
    }

    /**
     * Test tryLock method
     */
    @Test
    public void testTryLock() {
        singleConnectionDataSource.setLock(new ReentrantLock());
        boolean expectedResult = true;
        boolean actualResult = singleConnectionDataSource.tryLock();
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test onConnectionCreated method
     */
    @Test
    public void testOnConnectionCreated() throws SQLException {
        singleConnectionDataSource.setEventPublisher(mockEventPublisher);
        singleConnectionDataSource.onConnectionCreated(connection);
        Mockito.verify(mockEventPublisher, Mockito.never()).publishEvent(ArgumentMatchers.isA(ConnectionResetEvent.class));
    }

    @After
    public void tearDown() {
        singleConnectionDataSource.close();
    }
}
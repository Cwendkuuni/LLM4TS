package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.ConnectionWrapper;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ErrorCodes;
import com.oceanbase.odc.core.datasource.OceanbaseSingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ReentrantLock;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SingleConnectionDataSourceTest {
    private static final int MAX_RETRIES = 5;
    private static final long TIMEOUT_MILLIS = 10 * 1000;
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_DRIVER = "com.oceanbase.jdbc.OceanBaseDriver";
    private static final String TEST_URL = "jdbc:obcds://localhost:3306/testdb";
    private OceanbaseSingleConnectionDataSource singleConnectionDataSource;
    private ConnectionWrapper testConnection;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private Connection testConnectionMock;
    @Mock
    private CloseIgnoreInvocationHandler closeIgnoreInvocationHandler;
    @Mock
    private InvocationHandler invocationHandler;
    @Mock
    private Method closeMethod;
    @Mock
    private Object[] closeArgs;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.singleConnectionDataSource = new OceanbaseSingleConnectionDataSource();
        this.singleConnectionDataSource.setEventPublisher(this.eventPublisher);
        try {
            Class<? extends Driver> driverClass = (Class<? extends Driver>) Class.forName(TEST_DRIVER);
            Driver driver = driverClass.newInstance();
            DriverManager.registerDriver(driver);
        } catch (Exception e) {
            fail("Failed to register JDBC driver: " + TEST_DRIVER);
        }
        this.testConnection = new ConnectionWrapper(mock(Connection.class));
        when(this.testConnectionMock.isClosed()).thenReturn(false);
        when(this.testConnectionMock.isValid(TIMEOUT_MILLIS)).thenReturn(true);
        when(this.testConnectionMock.unwrap(ConnectionWrapper.class)).thenReturn(this.testConnection);
        when(this.testConnectionMock.hashCode()).thenReturn(RandomUtils.nextInt());
        when(this.testConnectionMock.equals(this.testConnectionMock)).thenReturn(true);
        this.singleConnectionDataSource.addInitializer(this.connectionInitializer);
    }

    @After
    public void tearDown() throws Exception {
        if (this.singleConnectionDataSource.getConnection() != null) {
            this.singleConnectionDataSource.close();
        }
        DriverManager.deregisterDriver(new OceanBaseDriver());
    }

    @Test
    public void testGetConnection() throws Exception {
        when(this.testConnectionMock.getAutoCommit()).thenReturn(null);
        when(this.testConnectionMock.isValid(TIMEOUT_MILLIS)).thenReturn(true);
        Connection conn = this.singleConnectionDataSource.getConnection();
        assertSame(this.testConnectionMock, conn);
        verify(this.testConnectionMock).isValid(TIMEOUT_MILLIS);
        verify(this.closeIgnoreInvocationHandler).invoke(this.closeIgnoreInvocationHandler,
                ArgumentMatchers.any(Method.class), ArgumentMatchers.any(Object[].class));
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() throws Exception {
        when(this.testConnectionMock.getAutoCommit()).thenReturn(null);
        when(this.testConnectionMock.isValid(TIMEOUT_MILLIS)).thenReturn(false);
        try {
            this.singleConnectionDataSource.getConnection(TEST_USERNAME, TEST_PASSWORD);
            fail();
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testGetConnectionWithNullCredentials() throws Exception {
        when(this.testConnectionMock.isValid(TIMEOUT_MILLIS)).thenReturn(true);
        try {
            this.singleConnectionDataSource.getConnection(null, null);
            fail();
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        when(this.connectionInitializer.init(this.testConnectionMock)).thenReturn(null);
        Connection connection = this.singleConnectionDataSource.innerCreateConnection();
        assertNotNull(connection);
        verify(this.connectionInitializer).init(this.testConnectionMock);
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnectionWithConnectionNotNull() throws Exception {
        this.singleConnectionDataSource.connection = this.testConnectionMock;
        this.singleConnectionDataSource.innerCreateConnection();
        fail();
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() throws Exception {
        when(this.testConnectionMock.isValid(TIMEOUT_MILLIS)).thenReturn(false);
        this.singleConnectionDataSource.resetConnection();
        fail();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer1 = mock(ConnectionInitializer.class);
        ConnectionInitializer connectionInitializer2 = mock(ConnectionInitializer.class);
        this.singleConnectionDataSource.addInitializer(connectionInitializer1);
        this.singleConnectionDataSource.addInitializer(connectionInitializer2);
        assertEquals(2, this.singleConnectionDataSource.initializerList.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddInitializerWithNullConnectionInitializer() {
        try {
            this.singleConnectionDataSource.addInitializer(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testClose() throws Exception {
        try {
            this.singleConnectionDataSource.close();
            assertFalse(this.singleConnectionDataSource.getConnection() != null);
        } finally {
            this.singleConnectionDataSource.resetConnection();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSetEventPublisherWithNullEventPublisher() {
        try {
            this.singleConnectionDataSource.setEventPublisher(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSetEventPublisher() throws Exception {
        this.singleConnectionDataSource.setEventPublisher(this.eventPublisher);
        verify(this.eventPublisher).publishEvent(ArgumentMatchers.instanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testOnConnectionReset() throws Exception {
        ConnectionResetEvent connectionResetEvent = mock(ConnectionResetEvent.class);
        this.singleConnectionDataSource.setEventPublisher(this.eventPublisher);
        this.singleConnectionDataSource.onConnectionReset(this.testConnectionMock);
        verify(this.eventPublisher).publishEvent(connectionResetEvent);
    }

    @Test
    public void testTryLock() throws Exception {
        assertTrue(this.singleConnectionDataSource.tryLock());
    }

    @Test
    public void testTryLockWithTimeout() throws Exception {
        assertFalse(this.singleConnectionDataSource.tryLock(TIMEOUT_MILLIS - 1, TimeUnit.MILLISECONDS));
    }

    @Test(expected = InterruptedException.class)
    public void testTryLockWithInterrupted() throws Exception {
        this.singleConnectionDataSource.lock.lock();
        this.singleConnectionDataSource.lock.isInterrupted();
        this.singleConnectionDataSource.tryLock();
        fail();
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.isValid(TIMEOUT_MILLIS)).thenReturn(true);
        when(this.testConnectionMock.unwrap(ConnectionWrapper.class)).thenReturn(this.testConnection);
        Connection connectionProxy = this.singleConnectionDataSource.getConnectionProxy(connection);
        assertNotNull(connectionProxy);
        verify(this.testConnection).tryLock();
        verify(this.testConnection).unlock();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithConnectionBusy() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.isValid(TIMEOUT_MILLIS)).thenReturn(false);
        Connection connectionProxy = this.singleConnectionDataSource.getConnectionProxy(connection);
        fail();
    }

    @Test
    public void testInitConnection() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.isValid(TIMEOUT_MILLIS)).thenReturn(true);
        this.singleConnectionDataSource.initConnection(connection);
        verify(this.connectionInitializer).init(connection);
    }

    @Test(expected = SQLException.class)
    public void testInitConnectionWithInvalidConnection() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.isValid(TIMEOUT_MILLIS)).thenReturn(false);
        this.singleConnectionDataSource.initConnection(connection);
        fail();
    }

    @Test
    public void testInvokeCloseMethod() throws Exception {
        List<Object> invocationArguments = new ArrayList<>();
        try {
            Method closeMethod = this.closeIgnoreInvocationHandler.getClass().getDeclaredMethod("invoke",
                    Method.class, Object[].class);
            closeMethod.setAccessible(true);
            Object result = closeMethod.invoke(this.closeIgnoreInvocationHandler, this.closeMethod, this.closeArgs);
            assertEquals(null, result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Failed to invoke close method: " + e.getMessage());
        }
    }

    @Test
    public void testInvokeCloseMethodWithNullMethod() throws Exception {
        try {
            Object result = this.closeIgnoreInvocationHandler.invoke(this.closeIgnoreInvocationHandler, null, this.closeArgs);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInvokeCloseMethodWithNullArgs() throws Exception {
        try {
            Object result = this.closeIgnoreInvocationHandler.invoke(this.closeIgnoreInvocationHandler, this.closeMethod, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.ErrorCodes;
import com.oceanbase.odc.core.datasource.EventPublisherFactory;
import com.oceanbase.odc.core.datasource.OceanbaseDataSourceFactory;
import com.oceanbase.odc.core.datasource.ReentrantLock;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionResetEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SingleConnectionDataSourceTest {
    private Connection connection;
    private EventPublisher eventPublisher;
    private ReentrantLock lock;
    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() throws Exception {
        Class<?> clazz = OceanbaseDataSourceFactory.class;
        Object obj = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {BaseClassBasedDataSource.class},
                new TestInvocationHandler());
        singleConnectionDataSource = (SingleConnectionDataSource) obj;
        singleConnectionDataSource.setAutoReconnect(true);
        singleConnectionDataSource.setEventPublisher(eventPublisher = Mockito.mock(EventPublisher.class));
        singleConnectionDataSource.setTimeOutMillis(5 * 1000);
        Connection testConnection = DriverManager.getConnection("jdbc:test:testDB", "test", "test");
        singleConnectionDataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                SingleConnectionDataSourceTest.this.connection = connection;
            }
        });
        singleConnectionDataSource.initialize();
    }

    @After
    public void tearDown() throws Exception {
        if (singleConnectionDataSource != null) {
            singleConnectionDataSource.close();
        }
    }

    @Test
    public void testGetConnection() throws Exception {
        assertNull(singleConnectionDataSource.getConnection());
        Connection connection = singleConnectionDataSource.getConnection();
        assertSame(connection, this.connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws Exception {
        assertNull(singleConnectionDataSource.getConnection("test", "test"));
        Connection connection = singleConnectionDataSource.getConnection("test", "test");
        fail("Expected SQLException: Invalid username or password");
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() throws Exception {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
        connection = singleConnectionDataSource.getConnection();
        assertSame(connection, this.connection);
    }

    @Test
    public void testResetConnectionWithoutAutoReconnect() throws Exception {
        singleConnectionDataSource.setAutoReconnect(false);
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
        try {
            singleConnectionDataSource.getConnection();
            fail("Expected SQLException: Connection was closed or not valid");
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    public void testAddInitializer() {
        List<ConnectionInitializer> initializerList = new ArrayList<>();
        singleConnectionDataSource.addInitializer(new ConnectionInitializer() {});
        assertEquals(1, singleConnectionDataSource.getInitializerList().size());
        singleConnectionDataSource.addInitializer(new ConnectionInitializer() {});
        assertEquals(2, singleConnectionDataSource.getInitializerList().size());
    }

    @Test(expected = SQLException.class)
    public void testClose() throws Exception {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.close();
        try {
            singleConnectionDataSource.getConnection();
            fail("Expected SQLException: Connection is closed");
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    public void testPrepareConnection() throws Exception {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.prepareConnection(connection);
        assertSame(connection.getAutoCommit(), singleConnectionDataSource.getAutoCommit());
    }

    @Test
    public void testTryLock() {
        assertTrue(singleConnectionDataSource.tryLock());
        assertFalse(singleConnectionDataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        Connection connection = singleConnectionDataSource.getConnection();
        Connection connectionProxy = singleConnectionDataSource.getConnectionProxy(connection);
        assertSame(connection, connectionProxy);
    }

    @Test
    public void testInitConnection() throws Exception {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.initConnection(connection);
        // assertions on ConnectionInitializer
    }

    @Test
    public void testOnConnectionReset() {
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.resetConnection();
        singleConnectionDataSource.onConnectionReset(connection);
        Mockito.verify(eventPublisher).publishEvent(Mockito.isA(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        Connection connection = singleConnectionDataSource.innerCreateConnection();
        assertNotNull(connection);
    }

    private static class TestInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(singleConnectionDataSource, args);
        }
    }
}
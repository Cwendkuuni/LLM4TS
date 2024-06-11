package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.test.mock.MockConnection;
import com.oceanbase.odc.core.datasource.test.mock.MockConnectionInitializer;
import com.oceanbase.odc.core.datasource.test.mock.MockEventPublisher;

/**
 * Tests for {@link SingleConnectionDataSource}.
 */
public class SingleConnectionDataSourceTest {

    private static final String USERNAME = "test_username";
    private static final String PASSWORD = "test_password";
    private static final long TIME_OUT_MILLIS = 10 * 1000;

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        this.dataSource = new SingleConnectionDataSource();
        this.eventPublisher = new MockEventPublisher();
        this.dataSource.setEventPublisher(this.eventPublisher);
        Mockito.doReturn(connection).when(this.dataSource).innerCreateConnection();
    }

    @Test
    public void testGetConnection() throws SQLException {
        this.dataSource.getConnection();
        assertNotNull(this.dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        this.dataSource.setUsername("invalid_username");
        this.dataSource.setPassword("invalid_password");
        this.dataSource.getConnection("test_username", "test_password");
        fail("Expected SQLException to be thrown");
    }

    @Test
    public void testResetConnection() throws SQLException {
        this.dataSource.resetConnection();
        assertNull(this.dataSource.getConnection());
        this.dataSource.setEventPublisher(this.eventPublisher);
        this.dataSource.resetConnection();
        verify(this.eventPublisher).publishEvent(Mockito.eq(new ConnectionResetEvent(connection)));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = new MockConnectionInitializer();
        this.dataSource.addInitializer(initializer);
        assertTrue(this.dataSource.initializerList.contains(initializer));
    }

    @Test
    public void testClose() throws SQLException {
        try (Connection connection = this.dataSource.getConnection();) {
            // Do nothing
        }
        this.dataSource.close();
        assertNull(this.dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        boolean autoCommit = true;
        Connection connection = new MockConnection();
        this.dataSource.prepareConnection(connection);
        assertTrue(connection.isAutoCommit() == autoCommit);
    }

    @Test
    public void testTryLock() {
        assertTrue(this.dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = new MockConnection();
        Connection connectionProxy = this.dataSource.getConnectionProxy(connection);
        assertNotNull(connectionProxy);
        assertTrue(connectionProxy instanceof CloseIgnoreInvocationHandler);
    }

    @Test
    public void testInitConnection() throws SQLException {
        List<ConnectionInitializer> initializerList = new ArrayList<>();
        ConnectionInitializer initializer = new MockConnectionInitializer();
        initializerList.add(initializer);
        this.dataSource.setInitializerList(initializerList);
        this.dataSource.initConnection(connection);
        verify(initializer).init(connection);
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        this.dataSource.onConnectionCreated(connection);
        // No verification needed, as there's no mock object to verify
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        this.dataSource.onConnectionReset(connection);
        verify(this.eventPublisher).publishEvent(Mockito.eq(new ConnectionResetEvent(connection)));
    }
}
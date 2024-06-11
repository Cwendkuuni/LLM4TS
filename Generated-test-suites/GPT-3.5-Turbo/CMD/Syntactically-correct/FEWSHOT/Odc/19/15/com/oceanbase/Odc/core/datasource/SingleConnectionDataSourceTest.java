package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SingleConnectionDataSourceTest {
  
    @Mock
    private EventPublisher eventPublisher;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource);
        assertNull(dataSource.getAutoCommit());
        assertEquals(false, dataSource.isAutoReconnect());
        assertEquals(10000, dataSource.getTimeOutMillis());
    }

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection conn = dataSource.getConnection();
        assertNotNull(conn);
        Connection conn2 = dataSource.getConnection("test", "password");
        assertNotNull(conn2);
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection conn = dataSource.getConnection();
        dataSource.resetConnection();
        Connection conn2 = dataSource.getConnection();
        assertNotNull(conn2);
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection conn = dataSource.getConnection();
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection conn = mock(Connection.class);
        dataSource.prepareConnection(conn);
    }

    @Test
    public void testTryLock() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        boolean locked = dataSource.tryLock();
        assertEquals(true, locked);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setEventPublisher(eventPublisher);
        dataSource.innerCreateConnection();
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Throwable {
        SingleConnectionDataSource.CloseIgnoreInvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(null, null);
        Connection conn = mock(Connection.class);
        handler.invoke(conn, conn.getClass().getMethod("isClosed"), null);
    }
}
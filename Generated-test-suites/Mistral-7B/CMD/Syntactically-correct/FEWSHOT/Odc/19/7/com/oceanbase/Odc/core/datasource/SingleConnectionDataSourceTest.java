package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;
    private ConnectionInitializer initializerMock;

    @Before
    public void setUp() {
        this.dataSource = new SingleConnectionDataSource();
        this.eventPublisher = mock(EventPublisher.class);
        this.initializerMock = mock(ConnectionInitializer.class);
        when(this.eventPublisher.publishEvent(ConnectionResetEvent.class)).thenReturn(true);
        this.dataSource.setEventPublisher(this.eventPublisher);
        this.dataSource.addInitializer(this.initializerMock);
    }

    @After
    public void tearDown() throws Exception {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    @Test
    public void testGetConnection() throws Exception {
        this.connection = mock(Connection.class);
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connection);

        Connection result = this.dataSource.getConnection();
        assertSame(this.connection, result);

        verify(this.initializerMock).init(this.connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws Exception {
        String username = "testUser";
        String password = "testPassword";
        when(this.dataSource.getUsername()).thenReturn(username);
        when(this.dataSource.getPassword()).thenReturn(password);

        this.dataSource.getConnection("wrongUser", "wrongPassword");
    }

    @Test
    public void testResetConnection() throws Exception {
        this.connection = mock(Connection.class);
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connection);
        this.dataSource.resetConnection();

        verify(this.eventPublisher).publishEvent(new ConnectionResetEvent(this.connection));
    }

    @Test(expected = ConflictException.class)
    public void testResetConnectionWithConnectionOccupied() throws Exception {
        this.dataSource.resetConnection();
        this.dataSource.resetConnection();
    }

    @Test
    public void testClose() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:mock:testDb", "user", "password");
        this.dataSource.setConnection(connection);
        this.dataSource.close();

        assertNull(this.dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        Connection connection = mock(Connection.class);
        this.dataSource.addInitializer(connection);

        assertFalse(this.dataSource.getInitializerList().isEmpty());
    }

    @Test
    public void testGetInitializerList() {
        Connection connection1 = mock(Connection.class);
        Connection connection2 = mock(Connection.class);
        this.dataSource.addInitializer(connection1);
        this.dataSource.addInitializer(connection2);

        List<Connection> initializerList = this.dataSource.getInitializerList();
        assertFalse(initializerList.isEmpty());
        assertTrue(initializerList.contains(connection1));
        assertTrue(initializerList.contains(connection2));
    }

    @Test
    public void testPrepareConnection() throws Exception {
        Connection connection = mock(Connection.class);
        boolean autoCommit = true;
        this.dataSource.prepareConnection(connection);
        verify(connection).setAutoCommit(autoCommit);
    }

    @Test
    public void testTryLock() throws InterruptedException {
        this.dataSource.setLock(new ReentrantLock());
        assertTrue(this.dataSource.tryLock(100, TimeUnit.MILLISECONDS));
        assertFalse(this.dataSource.tryLock(1, TimeUnit.MILLISECONDS));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithNullConnection() throws Exception {
        this.dataSource.connection = null;
        this.dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithClosedConnection() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);
        this.dataSource.setConnection(connection);
        this.dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidConnection() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.isValid(anyInt())).thenReturn(false);
        this.dataSource.setConnection(connection);
        this.dataSource.getConnection();
    }
}
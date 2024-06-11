package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.base.BaseClassBasedDataSource;

/**
 * Test class for SingleConnectionDataSource.
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;

    @Before
    public void setUp() {
        this.dataSource = new SingleConnectionDataSource();
        this.connectionMock = Mockito.mock(Connection.class);
        this.eventPublisherMock = Mockito.mock(EventPublisher.class);
        this.dataSource.setEventPublisher(this.eventPublisherMock);
    }

    @Test
    public void testGetConnection_DefaultConstructor() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnection_CustomConstructor() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnection_UsernameAndPassword() throws SQLException {
        String username = "testUsername";
        String password = "testPassword";
        this.dataSource.setUsername(username);
        this.dataSource.setPassword(password);
        Connection connection = this.dataSource.getConnection("username1", "password1");
        assertFalse(Objects.equals(username, "username1"));
        assertFalse(Objects.equals(password, "password1"));
        assertThrows(SQLException.class, () -> this.dataSource.getConnection("username2", "password2"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        this.dataSource.setConnection(connection);
        this.dataSource.resetConnection();
        assertNull(this.dataSource.getConnection());
        Mockito.verify(this.eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() throws SQLException {
        ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);
        this.dataSource.addInitializer(connectionInitializer);
        this.dataSource.getConnection();
        Mockito.verify(connectionInitializer).init(this.connectionMock);
    }

    @Test(expected = ConflictException.class)
    public void testConnectionOccupied() throws SQLException {
        this.dataSource.setLock(new ReentrantLock());
        this.dataSource.getConnectionProxy(this.connectionMock);
        this.dataSource.getConnectionProxy(this.connectionMock);
    }

    @Test(expected = SQLException.class)
    public void testInitConnection_NullConnection() throws SQLException {
        this.dataSource.initConnection(null);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        this.dataSource.setAutoCommit(false);
        Connection connection = this.dataSource.getConnection();
        this.dataSource.prepareConnection(connection);
        assertFalse(connection.getAutoCommit());
    }

    @Test
    public void testTryLock() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertFalse(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        Connection connectionProxy = this.dataSource.getConnectionProxy(connection);
        assertNotNull(connectionProxy);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        this.dataSource.setConnection(connection);
        this.dataSource.innerCreateConnection();
        Mockito.verify(this.eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }
}
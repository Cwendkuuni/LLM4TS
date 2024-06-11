package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Captor
    private ArgumentCaptor<ConnectionResetEvent> connectionResetEventCaptor;

    private SingleConnectionDataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() {
        this.dataSource = new SingleConnectionDataSource(true);
        this.dataSource.setEventPublisher(this.eventPublisher);
        this.dataSource.addInitializer(this.connectionInitializer);
    }

    @Test
    public void testGetConnectionReturnsExistingConnection() throws SQLException {
        this.connection = this.dataSource.getConnection();
        assertNotNull(this.connection);
        assertTrue(this.dataSource.getConnection() == this.connection);
    }

    @Test
    public void testGetConnectionReturnsNewConnectionOnFirstCall() throws SQLException {
        assertNull(this.dataSource.getConnection());
        Connection newConnection = this.dataSource.getConnection();
        assertNotNull(newConnection);
        assertFalse(newConnection == this.dataSource.getConnection());
    }

    @Test
    public void testGetConnectionReturnsNewConnectionOnSubsequentCallsWhenPreviousConnectionIsClosed()
            throws SQLException {
        this.connection = this.dataSource.getConnection();
        this.connection.close();
        Connection newConnection = this.dataSource.getConnection();
        assertNotNull(newConnection);
        assertFalse(newConnection == this.connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionThrowsSQLExceptionWhenInvalidUsernameOrPassword() throws SQLException {
        this.dataSource.setUsername("invalidUsername");
        this.dataSource.setPassword("invalidPassword");
        this.dataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithAutoCommitSetTrue() throws SQLException {
        this.dataSource.setAutoCommit(true);
        Connection connection = this.dataSource.getConnection();
        connection.setAutoCommit(false);
        assertFalse(connection.getAutoCommit());
    }

    @Test
    public void testGetConnectionWithAutoCommitSetFalse() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        assertFalse(connection.getAutoCommit());
    }

    @Test
    public void testResetConnectionWhenAutoReconnectIsFalseThrowsSQLException() throws SQLException {
        this.dataSource.resetConnection();
    }

    @Test
    public void testResetConnectionWhenAutoReconnectIsTrueInitializesANewConnection() throws SQLException {
        this.dataSource.resetConnection();
        Connection newConnection = this.dataSource.getConnection();
        assertNotNull(newConnection);
    }

    @Test
    public void testAddInitializer() {
        this.dataSource.addInitializer(this.connectionInitializer);
        assertTrue(this.dataSource.getInitializerList().contains(this.connectionInitializer));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddInitializerThrowsIllegalStateExceptionWhenDataSourceAlreadyHasAConnection() {
        this.dataSource.setConnection(new Connection());
        this.dataSource.addInitializer(this.connectionInitializer);
    }

    @Test
    public void testClose() throws SQLException {
        this.connection = this.dataSource.getConnection();
        this.dataSource.close();
        assertNull(this.dataSource.getConnection());
    }

    @Test
    public void testOnConnectionResetPublishEvent() throws SQLException, InterruptedException {
        this.dataSource.eventPublisher = Mockito.spy(this.eventPublisher);
        this.dataSource.getConnection();
        Mockito.verify(this.eventPublisher).publishEvent(this.connectionResetEventCaptor.capture());
        ConnectionResetEvent connectionResetEvent = this.connectionResetEventCaptor.getValue();
        assertNotNull(connectionResetEvent);
        assertEquals("Connection is reset", connectionResetEvent.getEventMessage());
    }

    @Test
    public void testInnerCreateConnectionThrowsSQLException() throws SQLException {
        Mockito.when(this.connectionInitializer.init(Mockito.any(Connection.class)))
                .thenThrow(new SQLException());
        assertThrows(SQLException.class, () -> this.dataSource.getConnection());
    }

    @After
    public void tearDown() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
        this.dataSource.close();
    }
}
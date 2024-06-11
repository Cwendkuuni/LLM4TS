package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisherImpl;
import com.oceanbase.odc.core.datasource.ReentrantLock;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test cases for SingleConnectionDataSource class
 *
 * @author yh263208
 * @date 2023-03-20
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    private EventPublisher eventPublisher;
    private Connection connection;
    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer connectionInitializerMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.eventPublisher = new EventPublisherImpl();
        this.connectionInitializerMock = mock(ConnectionInitializer.class);
        this.dataSource = new SingleConnectionDataSource() {
            {
                this.eventPublisher = this.eventPublisher;
                this.initializerList.add(this.connectionInitializerMock);
            }
        };

        try {
            Class.forName("com.oceanbase.jdbc.OceanBaseDriver");
            this.connection = DriverManager.getConnection("jdbc:oceanbase:odps://localhost:2181/test", "username", "password");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(this.connectionInitializerMock.init(this.connection)).thenReturn(null);

        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);
        verify(this.connectionInitializerMock).init(this.connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionNullCredentials() throws SQLException {
        this.dataSource.setUsername(null);
        this.dataSource.setPassword(null);
        this.dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws SQLException {
        this.dataSource.setUsername("invalidUsername");
        this.dataSource.setPassword("invalidPassword");
        this.dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        this.dataSource.resetConnection();
        assertNull(this.dataSource.getConnection());
        verify(this.eventPublisher).publishEvent(argThat(event -> event instanceof ConnectionResetEvent));
    }

    @Test
    public void testAddInitializer() {
        this.dataSource.addInitializer(this.connectionInitializerMock);
        assertTrue(this.dataSource.getInitializerList().contains(this.connectionInitializerMock));
    }

    @Test
    public void testClose() throws SQLException {
        this.dataSource.close();
        assertNull(this.dataSource.getConnection());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionTimeout() throws SQLException, InterruptedException {
        this.dataSource.setTimeOutMillis(100);
        doNothing().when(this.connectionInitializerMock).init(this.connection);
        doThrow(new SQLException("Connection timeout")).when(this.dataSource).innerCreateConnection();
        this.dataSource.getConnection();
    }

    @Test(expected = ConflictException.class)
    public void testConnectionOccupied() throws SQLException, InterruptedException {
        this.dataSource.setTimeOutMillis(100);
        this.dataSource.lock = new ReentrantLock();
        doThrow(new InterruptedException("Thread was interrupted")).when(this.dataSource.lock).tryLock(anyLong(), anyLong());
        this.dataSource.getConnection();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connectionProxy = this.dataSource.getConnectionProxy(this.connection);
        assertNotNull(connectionProxy);
        assertSame(this.connection, connectionProxy);
    }

    @Test
    public void testInitConnection() throws SQLException {
        List<ConnectionInitializer> initializerList = new ArrayList<>();
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource() {
            {
                this.eventPublisher = null;
                this.initializerList = initializerList;
            }
        };
        Connection connection = mock(Connection.class);

        doNothing().when(this.connectionInitializerMock).init(connection);

        dataSource.initConnection(connection);
        verify(this.connectionInitializerMock, times(1)).init(connection);
    }
}
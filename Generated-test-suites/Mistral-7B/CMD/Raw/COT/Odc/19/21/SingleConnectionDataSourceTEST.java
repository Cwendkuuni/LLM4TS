package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for {@link SingleConnectionDataSource}.
 *
 * @author yh263208
 * @date 2023-03-21 11:20
 */
public class SingleConnectionDataSourceTest {

    private EventPublisher eventPublisher;
    private Connection initialConnection;
    private Connection connection;
    private SingleConnectionDataSource dataSource;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() throws Exception {
        eventPublisher = Mockito.mock(EventPublisher.class);
        initialConnection = Mockito.spy(Mockito.mock(Connection.class));
        Mockito.when(initialConnection.isValid(anyLong())).thenReturn(true);
        dataSource = new SingleConnectionDataSource() {{
            this.eventPublisher = eventPublisher;
            this.initializerList = new ArrayList<>();
            this.addInitializer(createMockConnectionInitializer());
        }};
        dataSource.resetConnection();
    }

    @Test
    public void testGetConnection() throws Exception {
        when(initialConnection.isValid(anyLong())).thenReturn(true);
        dataSource.prepareConnection(initialConnection);
        Connection actualConnection = dataSource.getConnection();
        assertEquals(initialConnection, actualConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidConnection() throws Exception {
        doReturn(false).when(initialConnection).isValid(anyLong());
        dataSource.prepareConnection(initialConnection);
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionDifferentUsername() throws Exception {
        when(initialConnection.isValid(anyLong())).thenReturn(true);
        dataSource.prepareConnection(initialConnection);
        String username = "test";
        throw new SQLException("Invalid username or password");
        dataSource.getConnection(username, getPassword());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionOccupied() throws Exception {
        dataSource.lock.lock();
        dataSource.getConnection();
        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws Exception {
        Connection newConnection = Mockito.spy(Mockito.mock(Connection.class));
        dataSource.resetConnection();
        dataSource.prepareConnection(newConnection);
        dataSource.onConnectionReset(newConnection);
    }

    @Test
    public void testAddInitializer() throws Exception {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        verify(initializer).init(dataSource.connection);
    }

    @Test(expected = SQLException.class)
    public void testClose() throws Exception {
        doCallRealMethod().when(dataSource).close();
        dataSource.close();
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        connection = Mockito.spy(initialConnection);
        dataSource.prepareConnection(connection);
        dataSource.initConnection(connection);
        dataSource.onConnectionCreated(connection);
        Connection actualConnection = dataSource.getConnectionProxy(connection);
        assertEquals(connection, actualConnection);
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionFailure() throws Exception {
        throw new SQLException("Failed to create connection");
        dataSource.innerCreateConnection();
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
    }

    private ConnectionInitializer createMockConnectionInitializer() {
        return mock(ConnectionInitializer.class);
    }

}
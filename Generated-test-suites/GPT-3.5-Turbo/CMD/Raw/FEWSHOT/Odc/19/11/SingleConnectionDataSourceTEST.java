package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Mock
    private Connection mockConnection;
    @Mock
    private EventPublisher mockEventPublisher;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setEventPublisher(mockEventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernamePassword() throws SQLException {
        assertThrows(SQLException.class, () -> dataSource.getConnection("user", "password"));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testClose() {
        dataSource.setConnection(mockConnection);
        dataSource.close();
        verify(mockConnection).close();
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() {
        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(mockConnection));
    }

    @Test
    public void testInitConnection() throws SQLException {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.setConnection(mockConnection);

        dataSource.initConnection(mockConnection);
        
        verify(initializer).init(mockConnection);
    }

    @Test
    public void testOnConnectionCreated() {
        dataSource.onConnectionCreated(mockConnection);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        dataSource.innerCreateConnection();
        assertNotNull(dataSource.getConnection());
    }

}
package com.oceanbase.odc.core.datasource;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
        dataSource.setAutoReconnect(true);
    }

    @After
    public void tearDown() {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws Exception {
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernamePassword() throws Exception {
        assertNotNull(dataSource.getConnection("testUser", "testPassword"));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernamePassword() throws Exception {
        dataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws Exception {
        dataSource.resetConnection();
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(mockInitializer);
        dataSource.getConnection();
        verify(mockInitializer, times(1)).init(any());
    }

    @Test
    public void testClose() {
        Connection mockConnection = mock(Connection.class);
        dataSource.setConnection(mockConnection);
        dataSource.close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws Exception {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.getAutoCommit()).thenReturn(true);
        dataSource.setAutoCommit(false);
        dataSource.prepareConnection(mockConnection);
        verify(mockConnection, times(1)).setAutoCommit(false);
    }
}
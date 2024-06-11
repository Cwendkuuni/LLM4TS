package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    @Mock
    private Connection mockedConnection;
    @Mock
    private SingleConnectionDataSource.ConnectionInitializer mockedInitializer;
    @Mock
    private EventPublisher mockedPublisher;

    @Before
    public void setup() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        when(mockedConnection.getAutoCommit()).thenReturn(true);
        when(mockedConnection.isValid(anyInt())).thenReturn(true);
        dataSource.setLoginTimeout(1000);
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull(dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws Exception{
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testClose() {
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testSetAutoCommit() throws SQLException {
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getAutoCommit());
        
        dataSource.setAutoCommit(false);
        assertFalse(dataSource.getAutoCommit());
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(mockedInitializer);
        //unfortunately, without exposing the initializerList, there isn't a neat way to verify addInitializer, so this is a placeholder
    }

    @Test
    public void testSetEventPublisher() {
        dataSource.setEventPublisher(mockedPublisher);
        // Same issue with setEventPublisher, without a getter we cannot verify this functionality directly
    }
}


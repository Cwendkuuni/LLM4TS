//Test cases for SingleConnectionDataSource class

package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() {
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        String username = "test_user";
        String password = "test_password";
        assertNotNull(dataSource.getConnection(username, password));
    }

    @Test
    public void testResetConnection() {
        assertDoesNotThrow(() -> dataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() {
        assertDoesNotThrow(() -> dataSource.close());
    }

    @Test
    public void testAutoCommit() {
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getAutoCommit());
    }

    @Test
    public void testTimeOutMillis() {
        long timeout = 5000;
        dataSource.setTimeOutMillis(timeout);
        assertEquals(timeout, dataSource.getTimeOutMillis());
    }
}
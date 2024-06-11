package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
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
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid", "invalid"));
        assertNotNull(dataSource.getConnection(null, null));
    }

    @Test
    public void testResetConnection() {
        assertDoesNotThrow(() -> dataSource.resetConnection());
    }

    @Test
    public void testClose() {
        assertDoesNotThrow(() -> dataSource.close());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = connection -> {};
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

}
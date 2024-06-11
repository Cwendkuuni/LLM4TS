package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() {
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        String username = "testUser";
        String password = "testPassword";
        assertNotNull(dataSource.getConnection(username, password));
    }

    @Test
    public void testResetConnection() {
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = new ConnectionInitializer() {
            @Override
            public void init(Connection connection) {
                // Customize initialization logic here
            }
        };
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() {
        assertNotNull(dataSource.getConnection());
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() {
        assertNotNull(dataSource.getConnection());
        dataSource.prepareConnection(dataSource.getConnection());
        // Assert appropriate changes have been made to the connection
    }

    // Add more test cases as needed to comprehensively test all methods in SingleConnectionDataSource

}
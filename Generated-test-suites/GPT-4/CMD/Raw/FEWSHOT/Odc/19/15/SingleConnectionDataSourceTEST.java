
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource singleDataSource;

    @Before
    public void setUp() {
        singleDataSource = new SingleConnectionDataSource();
        singleDataSource.setUsername("testUser");
        singleDataSource.setPassword("testPass");
        singleDataSource.setAutoCommit(true);
    }

    @Test
    public void testAutoCommitGetterSetter() {
        assertEquals(true, singleDataSource.getAutoCommit());
        singleDataSource.setAutoCommit(false);
        assertEquals(false, singleDataSource.getAutoCommit());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        singleDataSource.getConnection("wrongUser", "wrongPass");
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = singleDataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection instanceof Connection);
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() throws SQLException {
        singleDataSource.resetConnection();
        assertNull(singleDataSource.connection);
    }

    @Test
    public void testClose() {
        Connection connection;
        try {
            connection = singleDataSource.getConnection();
            singleDataSource.close();
            // Assuming connection handling is done properly, this should not throw an Exception
            connection.isValid(1);
        } catch(SQLException e) {
            assertTrue(e.getMessage().contains("Connection was closed"));
        }
    }
}

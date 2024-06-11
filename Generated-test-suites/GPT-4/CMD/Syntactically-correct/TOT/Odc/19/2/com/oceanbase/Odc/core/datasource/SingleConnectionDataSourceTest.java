package com.oceanbase.odc.core.datasource;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceUnitTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        connection = Mockito.mock(Connection.class);
        Mockito.when(connection.isClosed()).thenReturn(false);
        Mockito.doNothing().when(connection).close();
    }

    @Test
    public void testGetConnection() {
        assertNotNull(dataSource);
        try {
            dataSource.initConnection(connection);
        } catch (SQLException e) {
            fail("Error initializing the connection.");
        }
        try {
            Connection conn = dataSource.getConnection();
            assertNotNull(conn);
        } catch (SQLException e) {
            fail("Error retrieving the connection.");
        }
    }

    @Test
    public void testResetConnection() {
        assertNotNull(dataSource);
        try {
            dataSource.initConnection(connection);
        } catch (SQLException e) {
            fail("Error initializing the connection.");
        }
        try {
            dataSource.resetConnection();
        } catch (SQLException e) {
            fail("Error resetting the connection.");
        }
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(Mockito.mock(ConnectionInitializer.class));
        assertTrue(dataSource.initializerList.size() == 1);
    }

    @Test
    public void testClose() {
        assertNotNull(dataSource);
        dataSource.close();
        try {
            Mockito.verify(connection, Mockito.times(1)).close();
        } catch (SQLException e) {
            fail("Error verifying the closed connection.");
        }
    }

    @After
    public void tearDown() {
        dataSource = null;
    }

}
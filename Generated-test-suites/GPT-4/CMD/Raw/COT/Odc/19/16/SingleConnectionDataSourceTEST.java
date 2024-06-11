
package com.oceanbase.odc.core.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;

    @Before
    public void setup(){
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetAutoCommit() throws SQLException {
        dataSource.setAutoCommit(true);
        boolean autoCommit = dataSource.getAutoCommit();
        assertEquals(true, autoCommit);
    }

    @Test
    public void testGetAutoReconnect() throws SQLException {
        boolean autoReconnect = dataSource.getAutoReconnect();
        assertEquals(false, autoReconnect);
    }

    @Test
    public void testSetEventPublisher() throws SQLException {
        EventPublisher eventPublisher = new EventPublisher();
        dataSource.setEventPublisher(eventPublisher);
        assertEquals(eventPublisher, dataSource.getEventPublisher());
    }

    @Test
    public void testSetTimeOutMillis() throws SQLException {
        dataSource.setTimeOutMillis(2000);
        // Add some assertions here to verify the impact of setting the timeout
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        assertNotNull(conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithCredentials() throws SQLException {
        dataSource.getConnection("username", "password");
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() {
        try{
            dataSource.resetConnection();
        } catch (SQLException ex){
            // Handle the exception
        }
    }

    @Test
    public void testAddInitializer() throws SQLException {
        ConnectionInitializer initializer = new ConnectionInitializer() {
            @Override
            public void init(Connection connection) {
                // implementation of the initialization logic
            }
        };
        dataSource.addInitializer(initializer);
        // Add some assertions here to verify the impact of adding the initializer
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.close();
        // Add some assertions here to verify the impact of closing the dataSource
    }
}
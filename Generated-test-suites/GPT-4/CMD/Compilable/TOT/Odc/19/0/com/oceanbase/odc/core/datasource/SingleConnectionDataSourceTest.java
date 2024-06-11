package com.oceanbase.odc.core.datasource;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException{
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("test");
        dataSource.setPassword("test");
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testResetConnection() throws SQLException{
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("test");
        dataSource.setPassword("test");

        Connection connection1 = dataSource.getConnection();
        dataSource.resetConnection();
        Connection connection2 = dataSource.getConnection();

        assertNotEquals(connection1, connection2);
    }
}
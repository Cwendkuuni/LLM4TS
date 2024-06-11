package com.oceanbase.odc.core.datasource;
import org.junit.Assert;
import org.junit.Test;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(true);
        Assert.assertNotNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(true);
        Assert.assertNotNull(singleConnectionDataSource.getConnection("username", "password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(true);
        try {
            singleConnectionDataSource.resetConnection();
        } catch (SQLException e) {
            Assert.fail();
        }
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(
        true);
        try {
            singleConnectionDataSource.close();
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
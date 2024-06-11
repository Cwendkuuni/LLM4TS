package com.oceanbase.odc.core.datasource;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.exceptions.base.MockitoException;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    // code for setting up mock connections, event publishers, etc...

    @Test
    public void testGetConnection() throws SQLException {
        // Create an instance
        SingleConnectionDataSource singleDataSource = new SingleConnectionDataSource();

        try {
            Connection conn = singleDataSource.getConnection();
            Assert.assertNotNull("The returned connection should not be null", conn);
        } catch (SQLException e) {
            Assert.fail("Unexpected SQLException thrown");
        }
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws SQLException {
        SingleConnectionDataSource singleDataSource = new SingleConnectionDataSource();
        singleDataSource.getConnection("testuser", "wrong password");
    }

    @Test
    public void testResetConnection() {
        SingleConnectionDataSource singleDataSource = new SingleConnectionDataSource();
        try {
            singleDataSource.resetConnection();
            Connection conn = singleDataSource.getConnection();
            Assert.assertNotNull("The returned connection should not be null", conn);
        } catch (SQLException e) {
            Assert.fail("Unexpected SQLException thrown");
        }
    }

    @Test(expected = MockitoException.class)
    public void testAddInitializer() {
        ConnectionInitializer initializer = null;
        SingleConnectionDataSource singleDataSource = new SingleConnectionDataSource();
        singleDataSource.addInitializer(initializer);
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource singleDataSource = new SingleConnectionDataSource();
        singleDataSource.close();
    }

    @Test
    public void testSetEventPublisher() {
        EventPublisher publisher = null;
        SingleConnectionDataSource singleDataSource = new SingleConnectionDataSource();
        singleDataSource.setEventPublisher(publisher);
    }
}
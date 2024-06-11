package com.oceanbase.odc.core.datasource;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.Before;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setup() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() {
        assertNotNull(singleConnectionDataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        singleConnectionDataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.close();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer mockInitializer = Mockito.mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(mockInitializer);
        // Here you could have some assertions based on mockInitializer
    }
}
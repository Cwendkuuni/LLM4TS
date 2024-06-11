
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testConstructorWithoutParameter() {
        assertFalse(singleConnectionDataSource.getAutoReconnect());
    }

    @Test
    public void testConstructorWithParameter() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        assertTrue(dataSource.getAutoReconnect());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionException() throws SQLException {
        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithParametersException() throws SQLException {
        singleConnectionDataSource.getConnection("username", "password");
    }

    @Test
    public void testSetAutoCommit() {
        singleConnectionDataSource.setAutoCommit(false);
        assertFalse(singleConnectionDataSource.getAutoCommit());      
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
    }

    @Test(expected = SQLException.class)
    public void testInitConnection(Connection connection) throws SQLException {
        singleConnectionDataSource.initConnection(connection);
    }

    @Test(expected = SQLException.class)
    public void testPrepareConnection(Connection connection) throws SQLException {
        singleConnectionDataSource.prepareConnection(connection);
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionException() throws SQLException {
        singleConnectionDataSource.innerCreateConnection();
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.close();
    }

}

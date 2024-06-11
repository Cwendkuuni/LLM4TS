import org.junit.Test;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
    
    @Test
    public void testGetConnection() throws SQLException {
        assertNull(singleConnectionDataSource.getConnection());
        singleConnectionDataSource.resetConnection();
        assertNotNull(singleConnectionDataSource.getConnection());
    }
   
    @Test(expected=SQLException.class)
    public void testGetConnectionWithInvalidAuth() throws SQLException {
        singleConnectionDataSource.getConnection("test", "test");
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test(expected=NullPointerException.class)
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(null);
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.close();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test(expected=NullPointerException.class)
    public void testPrepareConnnection() throws SQLException {
        singleConnectionDataSource.prepareConnection(null);
    }
}
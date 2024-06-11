import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
 
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }
   
    @Test
    public void testNewSource() {
        assertEquals(false, dataSource.getAutoCommit());
        assertEquals(false, dataSource.isAutoReconnect());
    }

    @Test(expected = SQLException.class)
    public void shouldThrowExceptionWhenConnectingInvalidUserCredentials() throws SQLException {
        String randomUsername = "HelloWorld",
               randomPassword = "Test123";
        dataSource.getConnection(randomUsername, randomPassword);
    }

    //...additional tests here...

}
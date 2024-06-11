
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void autoCommitSetterAndGettersTest() {
        Boolean expectedValue = Boolean.TRUE;

        singleConnectionDataSource.setAutoCommit(expectedValue);

        Boolean actualValue = singleConnectionDataSource.getAutoCommit();

        assertEquals(expectedValue, actualValue); 
    }

    @Test
    public void isAutoReconnectGetterTest() {
        // Since default argument of autoReconnect is false, expected value is false
        Boolean expectedValue = Boolean.FALSE;

        Boolean actualValue = singleConnectionDataSource.getAutoReconnect();

        assertEquals(expectedValue, actualValue); 
    }
    
    @Test
    public void getConnectionTest() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        
        assertNotNull(connection);
    }
    
    @Test (expected = SQLException.class)
    public void getConnectionWithInvalidUsernamePasswordTest() throws SQLException {
        // triggering exception by passing invalid username password
        singleConnectionDataSource.getConnection("username", "password");
    }

    @Test
    public void addInitializerTest() throws SQLException {
        int initialSize = singleConnectionDataSource.initializerList.size();
        singleConnectionDataSource.addInitializer(new ConnectionInitializer());
        int finalSize = singleConnectionDataSource.initializerList.size();

        assertEquals(finalSize, initialSize + 1);
    }
    
    @Test
    public void resetConnectionTest() throws SQLException {
        singleConnectionDataSource.resetConnection();

        assertNull(singleConnectionDataSource.connection);
        assertNull(singleConnectionDataSource.lock);
    }

    @Test (expected = SQLException.class)
    public void innerCreateConnectionExceptionTest() throws SQLException {
        singleConnectionDataSource.connection = mock(Connection.class);
        // since connection attribute is not null, exception must be thrown
        singleConnectionDataSource.innerCreateConnection();
    }
    
    @Test
    public void innerCreateConnectionTest() throws SQLException {
        singleConnectionDataSource.innerCreateConnection();

        assertNotNull(singleConnectionDataSource.connection);
        assertNotNull(singleConnectionDataSource.lock);
    }

}

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        // Dummy ConnectionConfig and autoCommit
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        Boolean autoCommit = true;
        
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        
        assertNotNull(dataSource);
    }

    // Add more test cases for other public methods

}
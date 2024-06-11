import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true, true);
    }
    
    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }
    
    @Test
    public void testGetUsername() {
        String username = obConsoleDataSourceFactory.getUsername();
        assertNotNull(username);
    }
    
    @Test
    public void testGetPassword() {
        String password = obConsoleDataSourceFactory.getPassword();
        assertNotNull(password);
    }
    
    @Test
    public void testGetConnectType() {
        ConnectType connectType = obConsoleDataSourceFactory.getConnectType();
        assertNotNull(connectType);
    }
    
    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertNotNull(dialectType);
    }
    
    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }
    
    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory deepCopy = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(deepCopy);
    }
    
    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetUsername(mapper);
        verify(mapper, times(1)).map(anyString());
    }
    
    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetPassword(mapper);
        verify(mapper, times(1)).map(anyString());
    }
    
    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetHost(mapper);
        verify(mapper, times(1)).map(anyString());
    }
    
    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetPort(mapper);
        verify(mapper, times(1)).map(anyInt());
    }
    
    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetSchema(mapper);
        verify(mapper, times(1)).map(anyString());
    }
    
    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetParameters(mapper);
        verify(mapper, times(1)).map(anyMap());
    }

}
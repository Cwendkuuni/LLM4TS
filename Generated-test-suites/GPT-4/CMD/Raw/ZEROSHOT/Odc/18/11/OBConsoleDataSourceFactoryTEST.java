
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig config;
    
    @Before
    public void setUp() {
        config = mock(ConnectionConfig.class);
        factory = new OBConsoleDataSourceFactory(config, true);
    }
    
    @Test
    public void testGetUsername() {
        assertNotEquals(null, factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertNotEquals(null, factory.getPassword());
    }

    @Test
    public void testGetDataSource() {
        DataSource ds = factory.getDataSource();
        assertEquals(ds.getUsername(), factory.getUsername());
        assertEquals(ds.getPassword(), factory.getPassword());
        assertEquals(ds.getUrl(), factory.getJdbcUrl());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory copiedFactory = factory.deepCopy();
        assertEquals(factory.getUsername(), copiedFactory.getUsername());
        assertEquals(factory.getPassword(), copiedFactory.getPassword());
        assertEquals(factory.getJdbcUrl(), copiedFactory.getJdbcUrl());
    }
    
    @Test
    public void testResetUsername() {
        String initialUsername = factory.getUsername();
        factory.resetUsername((value) -> value + "_testResetUsername");
        assertNotEquals(initialUsername, factory.getUsername());
    }
    
    @Test
    public void testResetPassword() {
        String initialPassword = factory.getPassword();
        factory.resetPassword((value) -> value + "_testResetPassword");
        assertNotEquals(initialPassword, factory.getPassword());
    }
    
    @Test
    public void testResetHost() {
        String initialHost = factory.getHost();
        factory.resetHost((value) -> value + "_testResetHost");
        assertNotEquals(initialHost, factory.getHost());
    }

    @Test
    public void testResetPort() {
        Integer initialPort = factory.getPort();
        factory.resetPort((value) -> value + 1);
        assertNotEquals(initialPort, factory.getPort());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> initialParams = factory.getParameters();
        factory.resetParameters((value) -> {
            value.put("resetTestKey", "resetTestValue");
            return value;
        });
        assertNotEquals(initialParams, factory.getParameters());
    }
}

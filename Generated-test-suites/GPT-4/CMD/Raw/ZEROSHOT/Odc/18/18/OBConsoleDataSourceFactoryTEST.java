
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.ConnectType;

import org.junit.Test;
import org.junit.Before;
import java.util.HashMap;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig config;

    @Before
    public void setUp() {
        config = mock(ConnectionConfig.class);
        dataSourceFactory = new OBConsoleDataSourceFactory(config, true);
    }

    @Test
    public void testGetUsername() {
        String username = "admin";
        when(config.getUsername()).thenReturn(username);
        assertEquals(username, dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        String password = "pass123";
        when(config.getPassword()).thenReturn(password);
        assertEquals(password, dataSourceFactory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        ConnectType connectType = ConnectType.DEFAULT_TYPE;
        when(config.getType()).thenReturn(connectType);
        assertEquals(connectType, dataSourceFactory.getConnectType());
    }

    @Test
    public void testGetJdbcUrl() {
        String host = "localhost";
        int port = 3306;
        String defaultSchema = "test";
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("allowMultiQueries", "true");
        when(config.getHost()).thenReturn(host);
        when(config.getPort()).thenReturn(port);
        when(config.getDefaultSchema()).thenReturn(defaultSchema);
        when(config.getJdbcUrlParameters()).thenReturn(parameters);
        String expectedUrl = "jdbc:mysql://localhost:3306/test?allowMultiQueries=true";
        assertEquals(expectedUrl, dataSourceFactory.getJdbcUrl());
    }
    
    @Test
    public void testGetDialectType() {
        DialectType dialectType = DialectType.MYSQL;
        when(config.getDialectType()).thenReturn(dialectType);
        assertEquals(dialectType, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory clone = dataSourceFactory.deepCopy();
        assertEquals(dataSourceFactory.getDialectType(), clone.getDialectType());  
    }
}


package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.core.shared.constant.DialectType;
import javax.sql.DataSource;
import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private SSLFileEntry sslFileEntry;

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    private HashMap<String, String> parameters;

    @Before
    public void setup() {
        parameters = new HashMap<>();
        parameters.put("param1", "value1");
        parameters.put("param2", "value2");

        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getUsername()).thenReturn("testuser");
        when(connectionConfig.getPassword()).thenReturn("password");
        when(connectionConfig.getSslConfig()).thenReturn(new SSLConfig());
        when(connectionConfig.getDefaultSchema()).thenReturn("defaultSchema");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionConfig.getSslFileEntry()).thenReturn(sslFileEntry);
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(parameters);
        when(sslFileEntry.getKeyStoreFilePath()).thenReturn("store");
        when(sslFileEntry.getKeyStoreFilePassword()).thenReturn("password");

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetUsername() {
        assertEquals("testuser", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory cloned = obConsoleDataSourceFactory.deepCopy();
        assertEquals(obConsoleDataSourceFactory.getHost(), cloned.getHost());
        assertEquals(obConsoleDataSourceFactory.getPort(), cloned.getPort());
        assertEquals(obConsoleDataSourceFactory.getUsername(), cloned.getUsername());
        assertEquals(obConsoleDataSourceFactory.getPassword(), cloned.getPassword());
    }

    // similar tests can be written for `resetUsername`, `resetPassword`, `resetSchema`, `resetHost`, `resetPort`, and `resetParameters` methods
}

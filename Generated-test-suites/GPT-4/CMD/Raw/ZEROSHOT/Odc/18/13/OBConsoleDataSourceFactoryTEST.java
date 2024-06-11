
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = mock(ConnectionConfig.class);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setOBTenantName("testTenant");
        connectionConfig.setClusterName("testCluster");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
        assertEquals("\"testUser\"@testTenant#testCluster", factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        when(connectionConfig.getPassword()).thenReturn("testPassword");
        assertEquals("testPassword", obConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBTenantEndpoint obTenantEndpoint = mock(OBTenantEndpoint.class);
        ConnectionConfig.SSLConfig sslConfig = mock(ConnectionConfig.SSLConfig.class);
        ConnectionConfig.SSLFileEntry sslFileEntry = mock(ConnectionConfig.SSLFileEntry.class);

        when(connectionConfig.getEndpoint()).thenReturn(obTenantEndpoint);
        when(connectionConfig.getSslConfig()).thenReturn(sslConfig);
        when(connectionConfig.getSslFileEntry()).thenReturn(sslFileEntry);
        when(obTenantEndpoint.getProxyHost()).thenReturn("10.10.10.10");
        when(obTenantEndpoint.getProxyPort()).thenReturn(8080);
        when(sslConfig.getEnabled()).thenReturn(true);
        when(sslFileEntry.getKeyStoreFilePath()).thenReturn("testPath");
        when(sslFileEntry.getKeyStoreFilePassword()).thenReturn("testKeyStorePass");

        Map<String, String> expectedJdbcParams = new HashMap<>();
        expectedJdbcParams.put("socksProxyHost", "10.10.10.10");
        expectedJdbcParams.put("socksProxyPort", "8080");
        expectedJdbcParams.put("useSSL", "true");
        expectedJdbcParams.put("trustServerCertificate", "false");
        expectedJdbcParams.put("disableSslHostnameVerification", "true");
        expectedJdbcParams.put("trustStore", "testPath");
        expectedJdbcParams.put("trustStorePassword", "testKeyStorePass");
        expectedJdbcParams.put("keyStore", "testPath");
        expectedJdbcParams.put("keyStorePassword", "testKeyStorePass");

        assertEquals(expectedJdbcParams, OBConsoleDataSourceFactory.getJdbcParams(connectionConfig));
    }

    @Test
    public void testGetConnectType() {
        when(connectionConfig.getType()).thenReturn(ConnectType.REMOTE);
        assertEquals(ConnectType.REMOTE, obConsoleDataSourceFactory.getConnectType());
    }
}

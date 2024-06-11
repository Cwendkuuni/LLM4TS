import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

import javax.sql.DataSource;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setup() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getHost()).thenReturn("localhost");
        Mockito.when(connectionConfig.getPort()).thenReturn(3306);
        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        Mockito.when(connectionConfig.getUsername()).thenReturn("testuser");
        Mockito.when(connectionConfig.getPassword()).thenReturn("password");
        Mockito.when(connectionConfig.getOBTenantName()).thenReturn("tenant");

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        Assert.assertNotNull(obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        String expected = "\"testuser\"@tenant";
        Assert.assertEquals(expected, OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void testGetPassword() {
        String expected = "password";
        Assert.assertEquals(expected, OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        Assert.assertNotNull(params);
        Assert.assertFalse(params.isEmpty());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        Assert.assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        Assert.assertEquals(DialectType.OB_ORACLE, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory copy = obConsoleDataSourceFactory.deepCopy();
        Assert.assertNotNull(copy);
        Assert.assertEquals(obConsoleDataSourceFactory.getJdbcUrl(), copy.getJdbcUrl());
    }
}
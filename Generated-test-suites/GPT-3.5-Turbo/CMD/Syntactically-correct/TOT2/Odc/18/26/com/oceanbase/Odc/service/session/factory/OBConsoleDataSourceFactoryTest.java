import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private DataSource dataSourceMock;

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = dataSourceFactory.getDataSource();

        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = dataSourceFactory.getDialectType();

        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory copiedDataSourceFactory = dataSourceFactory.deepCopy();

        assertNotNull(copiedDataSourceFactory);
        assertEquals(OBConsoleDataSourceFactory.class, copiedDataSourceFactory.getClass());
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        ValueMapper<String> mapper = mock(ValueMapper.class);
        when(mapper.map("test")).thenReturn("newTest");

        dataSourceFactory.resetUsername(mapper);

        assertEquals("newTest", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        ValueMapper<String> mapper = mock(ValueMapper.class);
        when(mapper.map("test")).thenReturn("newTest");

        dataSourceFactory.resetPassword(mapper);

        assertEquals("newTest", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        ValueMapper<String> mapper = mock(ValueMapper.class);
        when(mapper.map("testHost")).thenReturn("newTestHost");

        dataSourceFactory.resetHost(mapper);

        assertEquals("newTestHost", dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        ValueMapper<Integer> mapper = mock(ValueMapper.class);
        when(mapper.map(3306)).thenReturn(3307);

        dataSourceFactory.resetPort(mapper);

        assertEquals(Integer.valueOf(3307), dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        ValueMapper<String> mapper = mock(ValueMapper.class);
        when(mapper.map("testSchema")).thenReturn("newTestSchema");

        dataSourceFactory.resetSchema(mapper);

        assertEquals("newTestSchema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        ValueMapper<Map<String, String>> mapper = mock(ValueMapper.class);

        Map<String, String> testParams = new HashMap<>();
        testParams.put("param1", "value1");
        testParams.put("param2", "value2");

        Map<String, String> newParams = new HashMap<>();
        newParams.put("param1", "newValue1");
        newParams.put("param2", "newValue2");

        when(mapper.map(testParams)).thenReturn(newParams);

        dataSourceFactory.resetParameters(mapper);

        assertEquals("newValue1", dataSourceFactory.getJdbcParams().get("param1"));
        assertEquals("newValue2", dataSourceFactory.getJdbcParams().get("param2"));
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        String jdbcUrl = dataSourceFactory.getJdbcUrl();

        assertNotNull(jdbcUrl);
    }
}
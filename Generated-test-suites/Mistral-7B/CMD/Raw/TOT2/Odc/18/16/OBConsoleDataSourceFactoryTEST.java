import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setup() {
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDefaultSchema()).thenReturn("test_schema");
        when(connectionConfig.getType()).thenReturn(ConnectType.OB_MYSQL);
        when(connectionConfig.getUsername()).thenReturn("test_user");
        when(connectionConfig.getPassword()).thenReturn("test_password");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyMap()))
                .thenReturn("jdbc:mock:myds://localhost:3306/test_schema");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn(DriverManager.class.getName());
    }

    @Test
    public void testGetDataSource() throws SQLException {
        PoolingDataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertEquals(DriverManagerDataSource.class, dataSource.getClass());
        Connection connection = dataSource.getConnection();
        assertEquals("jdbc:mock:myds://localhost:3306/test_schema", connection.getConnectionURL());
        assertNull(connection.getAutoCommit());
    }

    @Test
    public void testGetDataSource_withAutoCommit() throws SQLException {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        PoolingDataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        assertEquals("jdbc:mock:myds://localhost:3306/test_schema", connection.getConnectionURL());
        assertTrue(connection.getAutoCommit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_withNullConfig() {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(null, false);
        assertThrows(IllegalArgumentException.class, obConsoleDataSourceFactory::getDataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_withNullConnectionExtensionPoint() {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, false);
        obConsoleDataSourceFactory.connectionExtensionPoint = null;
        assertThrows(IllegalArgumentException.class, obConsoleDataSourceFactory::getDataSource);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataSource_withInvalidDialectType() {
        when(connectionConfig.getDialectType()).thenReturn(DialectType.INVALID);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, false);
        assertThrows(IllegalStateException.class, obConsoleDataSourceFactory::getDataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        assertEquals("jdbc:mock:myds://localhost:3306/test_schema", obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetJdbcUrl_withInvalidDialectType() {
        when(connectionConfig.getDialectType()).thenReturn(DialectType.INVALID);
        assertNull(obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals("test_user", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("test_password", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        assertEquals(ConnectType.OB_MYSQL, obConsoleDataSourceFactory.getConnectType());
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(Mockito.mock(OBConsoleDataSourceFactory.ValueMapper.class));
        assertEquals("test_user", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(Mockito.mock(OBConsoleDataSourceFactory.ValueMapper.class));
        assertEquals("test_password", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(Mockito.mock(OBConsoleDataSourceFactory.ValueMapper.class));
        assertEquals("localhost", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(Mockito.mock(OBConsoleDataSourceFactory.ValueMapper.class));
        assertEquals(3306, obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(Mockito.mock(OBConsoleDataSourceFactory.ValueMapper.class));
        assertEquals("test_schema", obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("test", "test");
        obConsoleDataSourceFactory.resetParameters(Mockito.mock(OBConsoleDataSourceFactory.ValueMapper.class));
        assertEquals(parameters, obConsoleDataSourceFactory.getParameters());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, false, false);
        OBConsoleDataSourceFactory clone = (OBConsoleDataSourceFactory) original.deepCopy();
        assertNotSame(original, clone);
        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getParameters(), clone.getParameters());
        assertEquals(original.getConnectType(), clone.getConnectType());
    }
}
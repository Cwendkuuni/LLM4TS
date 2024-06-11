import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DriverManagerDataSource;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private ConnectionSessionUtil connectionSessionUtil;

    private OBConsoleDataSourceFactory dataSourceFactory;
    private DataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(connectionExtensionPoint.generateJdbcUrl("localhost", 3306, "test_schema", null)).thenReturn("jdbc:test://localhost:3306/test_schema");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[] { connectionInitializer });

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        ConnectionConfig cloneConfig = ConnectionMapper.INSTANCE.clone(connectionConfig);
        this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        this.dataSource = this.dataSourceFactory.getDataSource();

        assertTrue(this.dataSource instanceof SingleConnectionDataSource);
        assertEquals("jdbc:test://localhost:3306/test_schema", this.dataSource.getUrl());
        assertEquals("test_user", this.dataSource.getUsername());
        assertEquals("test_password", this.dataSource.getPassword());
        assertEquals("com.mysql.jdbc.Driver", this.dataSource.getDriverClassName());

        // Test connection with connectionInitializer
        Connection connection = this.dataSource.getConnection();
        verify(this.connectionInitializer).initializeDatabase(connection);
        connection.close();
    }

    @Test
    public void testGetDataSourceWithEmptyPassword() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        OBConsoleDataSourceFactory emptyPasswordDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        DataSource emptyPasswordDataSource = emptyPasswordDataSourceFactory.getDataSource();

        // Test connection failure with empty password
        try {
            emptyPasswordDataSource.getConnection();
            fail();
        } catch (SQLException e) {
            assertEquals("Caused by: java.sql.SQLException: User authentication failure: Password can not be empty", e.getMessage());
        }
    }

    @Test
    public void testGetDataSourceWithInvalidPort() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(-1);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        OBConsoleDataSourceFactory invalidPortDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        DataSource invalidPortDataSource = invalidPortDataSourceFactory.getDataSource();

        // Test connection failure with invalid port
        try {
            invalidPortDataSource.getConnection();
            fail();
        } catch (SQLException e) {
            assertEquals("Caused by: java.sql.SQLException: Communications link failure: The last packet sent successfully to the server was 0, and the server was not able to send a response packet", e.getMessage());
        }
    }

    @Test
    public void testGetDataSourceWithEmptySchema() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("");
        connectionConfig.setDialectType(DialectType.MYSQL);
        OBConsoleDataSourceFactory emptySchemaDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        DataSource emptySchemaDataSource = emptySchemaDataSourceFactory.getDataSource();

        // Test connection with empty schema
        Connection connection = emptySchemaDataSource.getConnection();
        String schema = connection.createStatement().executeQuery("select schema_name from information_schema.schemata limit 1").getString(1);
        assertEquals("", schema);
        connection.close();
    }

    @Test
    public void testGetDataSourceWithDefaultSchema() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        OBConsoleDataSourceFactory defaultSchemaDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        DataSource defaultSchemaDataSource = defaultSchemaDataSourceFactory.getDataSource();

        // Test connection with default schema
        Connection connection = defaultSchemaDataSource.getConnection();
        String schema = connection.createStatement().executeQuery("select schema_name from information_schema.schemata limit 1").getString(1);
        assertEquals("test_schema", schema);
        connection.close();
    }

    @Test
    public void testGetDataSourceWithSsl() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);

        SSLConfig sslConfig = new SSLConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setTrustStoreFilePath("path/to/truststore.p12");
        sslFileEntry.setTrustStorePassword("password");
        sslConfig.setEnabled(true);
        sslConfig.setSslFileEntries(new SSLFileEntry[] { sslFileEntry });
        connectionConfig.setSslConfig(sslConfig);

        OBConsoleDataSourceFactory sslDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

        DataSource sslDataSource = sslDataSourceFactory.getDataSource();

        // Test connection with ssl
        Connection connection = sslDataSource.getConnection();
        connection.close();
    }

    @Test
    public void testGetDataSourceWithConnectionInitializer() throws SQLException {
        when(connectionInitializer.initializeDatabase(any(Connection.class))).thenReturn(true);

        DataSource dataSourceWithInitializer = this.dataSourceFactory.getDataSource();

        // Test connection with connectionInitializer
        Connection connection = dataSourceWithInitializer.getConnection();
        verify(this.connectionInitializer).initializeDatabase(connection);
        connection.close();

        // Test connection initialization order
        Connection connection2 = dataSourceWithInitializer.getConnection();
        verify(this.connectionInitializer, times(2)).initializeDatabase(connection2);
        connection2.close();
    }

    @Test
    public void testGetDataSourceWithoutConnectionInitializer() throws SQLException {
        ConnectionDataSource noInitializerDataSource = mock(PoolingDataSource.class);
        when(noInitializerDataSource.getConnection()).thenReturn(mock(Connection.class));

        DataSourceFactory dataSourceFactoryWithoutInitializer = new OBConsoleDataSourceFactory(this.connectionConfig, true);

        DataSource dataSourceWithoutInitializer = dataSourceFactoryWithoutInitializer.getDataSource();

        // Test connection without connectionInitializer
        Connection connection = dataSourceWithoutInitializer.getConnection();
        verifyNoMoreInteractions(this.connectionInitializer);
        connection.close();

        assertEquals(noInitializerDataSource, dataSourceWithoutInitializer);
    }

    @Test
    public void testClone() throws SQLException {
        DataSource clonedDataSource = this.dataSourceFactory.deepCopy().getDataSource();

        // Test cloned data source
        Connection clonedConnection = clonedDataSource.getConnection();
        assertEquals(this.dataSource.getUrl(), clonedConnection.getConnectionProperties().getProperty("url"));
        assertEquals(this.dataSource.getUsername(), clonedConnection.getUsername());
        assertEquals(this.dataSource.getPassword(), clonedConnection.getPassword());
        assertEquals(this.dataSource.getDriverClassName(), clonedConnection.getDriverClassName());
        clonedConnection.close();
    }

    @Test
    public void testCloneWithUpdatedProperties() throws SQLException {
        Map<String, String> updatedProperties = new HashMap<>();
        updatedProperties.put("url", "jdbc:test://localhost:3307/updated_schema");
        DataSourceFactory clonedDataSourceFactory = this.dataSourceFactory.deepCopy();
        when(connectionExtensionPoint.generateJdbcUrl("localhost", 3307, "updated_schema", null)).thenReturn("jdbc:test://localhost:3307/updated_schema");

        DataSource clonedDataSource = clonedDataSourceFactory.getDataSource();

        // Test cloned data source with updated properties
        Connection clonedConnection = clonedDataSource.getConnection();
        assertEquals(updatedProperties.get("url"), clonedConnection.getConnectionProperties().getProperty("url"));
        clonedConnection.close();
    }
}
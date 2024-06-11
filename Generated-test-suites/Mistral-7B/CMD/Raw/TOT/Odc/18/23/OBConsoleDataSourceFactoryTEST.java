package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    private DataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        connectionConfig.setDialectType(DialectType.MYSQL);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, false);
    }

    @After
    public void cleanup() throws SQLException {
        if (dataSourceFactory != null) {
            ((BasicDataSource) dataSourceFactory.getDataSource()).close();
        }
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataSource_WithoutInitialization() throws SQLException {
        dataSourceFactory.setInitConnection(false);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNull(dataSource);
    }

    @Test
    public void testCloneableDataSourceFactory_DeepCopy() throws SQLException {
        DataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals(dataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(dataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(dataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        assertEquals(dataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(dataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
        assertEquals(dataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
        DataSource clonedDataSource = clonedDataSourceFactory.getDataSource();
        assertNotNull(clonedDataSource);
    }

    @Test
    public void testCloneableDataSourceFactory_ResetValue() throws SQLException {
        String newUsername = "testUsername";
        String newPassword = "testPassword";
        String newHost = "testHost";
        int newPort = 3307;
        String newDefaultSchema = "testSchema";
        Map<String, String> newParameters = new HashMap<>();
        dataSourceFactory.setUsername(newUsername);
        dataSourceFactory.setPassword(newPassword);
        dataSourceFactory.setHost(newHost);
        dataSourceFactory.setPort(newPort);
        dataSourceFactory.setDefaultSchema(newDefaultSchema);
        dataSourceFactory.setParameters(newParameters);

        assertEquals(newUsername, dataSourceFactory.getUsername());
        assertEquals(newPassword, dataSourceFactory.getPassword());
        assertEquals(newHost, dataSourceFactory.getHost());
        assertEquals(newPort, dataSourceFactory.getPort());
        assertEquals(newDefaultSchema, dataSourceFactory.getDefaultSchema());
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testGetDataSource_Initialization() throws SQLException {
        dataSourceFactory.setInitConnection(true);
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();

        Connection newConnection = dataSource.getConnection();
        assertNotNull(newConnection);

        SessionCreatedInitializer initializer = (SessionCreatedInitializer) dataSourceFactory.getDataSource()
                .getConnectionInitializers().get(0);
        initializer.afterPropertiesSet();

        Connection newNewConnection = dataSource.getConnection();
        assertNotNull(newNewConnection);
    }

}
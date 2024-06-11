package com.oceanbase.odc.service.session.factory;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.ODCFactoryUtils;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.connector.OBCPooledConnection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    String host = "localhost";
    Integer port = 3306;
    String username = "test_username";
    String password = "test_password";
    String defaultSchema = "test_schema";


    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost(host);
        connectionConfig.setPort(port);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        Assert.assertEquals("Check if the JDBC URL is as expected", ODCFactoryUtils.constructJdbcUrl(host, port), dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(username);
        Assert.assertEquals("Check if the username is as expected", username, OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword(password);
        Assert.assertEquals("Check if the password is as expected", password, OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    @Test
    public void testGetDataSource() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost(host);
        connectionConfig.setPort(port);
        connectionConfig.setUsername(username);
        connectionConfig.setPassword(password);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        try(OBCPooledConnection connection = dataSourceFactory.getDataSource.getConnection()) {
            Assert.assertTrue("Check if connection is valid", connection.isValid(0));
        }
    }
}

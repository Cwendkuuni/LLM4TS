package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.hamcrest.CoreMatchers;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import javax.sql.DataSource;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private ConnectionExtensionPoint extensionPoint;
    private OBConsoleDataSourceFactory factory;
    private DataSource dataSource;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        extensionPoint = mock(ConnectionExtensionPoint.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);

        dataSource = mock(SingleConnectionDataSource.class);

        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.setConnectionExtensionPoint(extensionPoint);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "test@tenant#cluster";
        when(connectionConfig.getUsername()).thenReturn("test");
        when(connectionConfig.getOBTenantName()).thenReturn("tenant");
        when(connectionConfig.getTenantName()).thenReturn(null);
        when(connectionConfig.getClusterName()).thenReturn("cluster");

        String actualUsername = OBConsoleDataSourceFactory.getUsername(connectionConfig);

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "password";
        when(connectionConfig.getPassword()).thenReturn(expectedPassword);

        String actualPassword = OBConsoleDataSourceFactory.getPassword(connectionConfig);

        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clone = factory.deepCopy();

        assertEquals(factory.getDialectType(), clone.getDialectType());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = factory.getDataSource();

        assertThat(dataSource, CoreMatchers.instanceOf(SingleConnectionDataSource.class));
    }

    @Test
    public void testGetDefaultSchema() {
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(connectionConfig.getDefaultSchema()).thenReturn(OdcConstants.MYSQL_DEFAULT_SCHEMA);

        String expectedSchema = OdcConstants.MYSQL_DEFAULT_SCHEMA;
        String actualSchema = OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig);

        assertEquals(expectedSchema, actualSchema);
    }

}
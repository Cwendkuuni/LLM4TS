package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.ConnectType;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionConfig connectionConfig;

    private OBConsoleDataSourceFactory factory;
    private String username = "test_username";
    private String password = "test_password";
    private String host = "localhost";
    private Integer port = 8080;
    private String defaultSchema = "test_schema";
    private Map<String, String> parameters = new HashMap<>();
    private Boolean autoCommit = true;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(connectionConfig.getUsername()).thenReturn(username);
        when(connectionConfig.getPassword()).thenReturn(password);
        when(connectionConfig.getHost()).thenReturn(host);
        when(connectionConfig.getPort()).thenReturn(port);
        when(connectionConfig.getDefaultSchema()).thenReturn(defaultSchema);
        when(connectionConfig.isAutoCommit()).thenReturn(autoCommit);
        factory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void testGetUsername() {
        assertEquals(factory.getUsername(), username);
    }

    @Test
    public void testGetPassword() {
        assertEquals(factory.getPassword(), password);
    }

    @Test
    public void testGetConnectType() {
        when(connectionConfig.getType()).thenReturn(ConnectType.TCP);
        assertEquals(factory.getConnectType(), ConnectType.TCP);
    }

    @Test
    public void testGetDataSource() {
        DataSource actualDataSource = factory.getDataSource();
        assertNotNull(actualDataSource);
        assertEquals(factory.getDataSource(), actualDataSource);
    }

    @Test
    public void testGetDialectType() {
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        assertEquals(DialectType.OB_ORACLE, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory copy = factory.deepCopy();
        assertEquals(copy.getUsername(), factory.getUsername());
        assertEquals(copy.getPassword(), factory.getPassword());
        assertEquals(copy.getHost(), factory.getHost());
        assertEquals(copy.getPort(), factory.getPort());
        assertEquals(copy.getSchema(), factory.getSchema());
        assertEquals(copy.getParameters(), factory.getParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPort() {
        factory.resetPort(x -> -1);
    }

    @Test
    public void testResetSchema() {
        String updatedSchema = "test_schema2";
        factory.resetSchema(x -> updatedSchema);
        assertEquals(updatedSchema,factory.getSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> updatedParams = new HashMap<>();
        updatedParams.put("testParam", "testValue");
        factory.resetParameters(x -> updatedParams);
        assertEquals(updatedParams,factory.getParameters());
    }
}

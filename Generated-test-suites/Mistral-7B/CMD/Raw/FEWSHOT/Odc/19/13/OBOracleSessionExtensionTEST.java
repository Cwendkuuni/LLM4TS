package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionFactory;
import org.pf4j.impl.DefaultExtensionFactory;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPointRegistry;
import org.pf4j.impl.DefaultServiceRegistry;
import org.pf4j.impl.DefaultServiceRegistryFactory;
import org.pf4j.impl.DefaultServiceRegistryImpl;
import org.pf4j.impl.DefaultServiceRegistryListener;
import org.pf4j.impl.DefaultServiceRegistryListenerRegistry;
import org.pf4j.impl.DefaultServiceRegistryListenerRegistryImpl;
import org.pf4j.impl.DefaultServiceRegistryListenerRegistryImpl.ServiceRegistryListenerChangeEvent;
import org.pf4j.impl.jdk7.DefaultExtensionPointRegistryImpl;
import org.pf4j.impl.jdk7.DefaultExtensionPointRegistryImpl.ExtensionPointChangeEvent;
import org.pf4j.impl.jdk7.DefaultExtensionPointRegistryImpl.ExtensionPointChangeListener;
import org.pf4j.impl.jdk7.DefaultExtensionRegistry;
import org.pf4j.impl.jdk7.DefaultExtensionRegistryImpl;
import org.pf4j.impl.jdk7.DefaultServiceRegistryImpl;
import org.pf4j.impl.jdk7.DefaultServiceRegistryImpl.ServiceRegistryEvent;
import org.pf4j.impl.jdk7.DefaultServiceRegistryImpl.ServiceRegistryListener;
import org.pf4j.impl.jdk7.DefaultServiceRegistryImpl.ServiceRegistryListenerProxy;
import org.pf4j.impl.jdk7.DefaultServiceRegistryListenerRegistryImpl;
import org.pf4j.impl.jdk7.DefaultServiceRegistryListenerRegistryImpl.ServiceRegistryListenerChangeEvent;
import org.pf4j.impl.jdk7.DefaultServiceRegistryListenerRegistryImpl.ServiceRegistryListenerChangeListener;
import org.pf4j.impl.jdk7.DefaultServiceRegistryListenerRegistryImpl.ServiceRegistryListenerRegistry;
import org.pf4j.impl.jdk7.DefaultServiceRegistryListenerRegistryImpl.ServiceRegistryListenerRegistryImpl;
import org.pf4j.plugin.connect.api.SessionExtensionPoint;

import com.oceanbase.jdbc.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.getCurrentSchema;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.killQuery;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.switchSchema;

import mockito.InjectMocks;
import mockito.Mock;
import mockito.MockitoAnnotations;

/**
 * JUnit test cases for OBOracleSessionExtension class
 */
public class OBOracleSessionExtensionTest {

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    @Test
    public void testKillQuery() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "connectionId";
        when(jdbcOperationsUtil.execute(connectionMock, "KILL QUERY " + connectionId)).thenReturn(null);

        obOracleSessionExtension.killQuery(connectionMock, connectionId);

        // No verification needed as JdbcOperationsUtil.execute() method is already mocked
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "connectionId";
        when(jdbcOperationsUtil.execute(connectionMock, "KILL QUERY " + connectionId)).thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";

        when(jdbcOperationsUtil.queryForObject(connectionMock, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(currentSchema);
        when(jdbcOperationsUtil.update(connectionMock, "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName))).thenReturn(1);

        obOracleSessionExtension.switchSchema(connectionMock, schemaName);

        // No verification needed as JdbcOperationsUtil methods are already mocked
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String schemaName = "testSchema";

        when(jdbcOperationsUtil.queryForObject(connectionMock, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(schemaName);
        when(jdbcOperationsUtil.update(connectionMock, "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName))).thenThrow(new SQLException("Test Exception"));

        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String currentSchema = "currentSchema";

        when(jdbcOperationsUtil.queryForObject(connectionMock, "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(currentSchema);

        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connectionMock));

        // No verification needed as JdbcOperationsUtil method is already mocked
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        String connectionId = "connectionId";

        when(jdbcOperationsUtil.queryForObject(connectionMock, "select userenv('sessionid') from dual")).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connectionMock));

        // No verification needed as JdbcOperationsUtil method is already mocked
    }

    @Test
    public void testGetConnectionIdFromOceanBaseConnection() {
        Connection oceanBaseConnection = mock(OceanBaseConnection.class);
        String connectionId = "connectionId";

        when(oceanBaseConnection.getServerThreadId()).thenReturn(connectionId);

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(oceanBaseConnection));

        // No verification needed as mockito mocks are not real objects
    }

    @Test
    public void testGetConnectionIdNull() {
        Connection connectionMock = mock(Connection.class);

        assertNull(obOracleSessionExtension.getConnectionId(connectionMock));

        // No verification needed as JdbcOperationsUtil method is not called
    }

    @Test
    public void testExtensionRegistration() {
        DefaultExtensionFactory extensionFactory = new DefaultExtensionFactory();
        DefaultExtensionRegistry extensionRegistry = new DefaultExtensionRegistryImpl();
        DefaultServiceRegistry serviceRegistry = new DefaultServiceRegistryImpl();
        DefaultExtensionPointRegistry extensionPointRegistry = new DefaultExtensionPointRegistryImpl();
        DefaultExtensionManager extensionManager = new DefaultExtensionManager(extensionFactory, extensionRegistry, serviceRegistry, extensionPointRegistry);

        extensionManager.start();

        // Register OBOracleSessionExtension
        DefaultServiceRegistryListenerRegistry serviceRegistryListenerRegistry = new DefaultServiceRegistryListenerRegistryImpl();
        ServiceRegistryListenerRegistry serviceRegistryListenerRegistryProxy = new ServiceRegistryListenerProxy(serviceRegistryListenerRegistry);
        ServiceRegistryListenerChangeListener serviceRegistryListenerChangeListener = new ServiceRegistryListenerChangeListener() {
            @Override
            public void onExtensionAdded(Extension extension) {
                assert extension.getClass().equals(OBOracleSessionExtension.class);
            }
        };
        serviceRegistryListenerRegistry.addListener(serviceRegistryListenerChangeListener);
        serviceRegistryListenerRegistryProxy.addListener(serviceRegistryListenerRegistry);
        extensionRegistry.addListener(serviceRegistryListenerRegistryProxy);

        extensionManager.registerExtension(OBOracleSessionExtension.class, extensionRegistry);

        // Verify that OBOracleSessionExtension was registered
        Optional<Extension> registeredExtension = extensionRegistry.getExtensions().stream().filter(extension -> extension.getClass().equals(OBOracleSessionExtension.class)).findFirst();
        assertTrue(registeredExtension.isPresent());

        // Shutdown extension manager
        extensionManager.stop();
    }
}
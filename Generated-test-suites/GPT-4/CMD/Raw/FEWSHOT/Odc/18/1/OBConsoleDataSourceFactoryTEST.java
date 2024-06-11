
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.DialectType;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    private ConnectionConfig connectionConfig;
    private EventPublisher eventPublisher;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        eventPublisher = mock(EventPublisher.class);
    }

    @Test
    public void testGetDataSource() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.setEventPublisher(eventPublisher);

        assertNotNull(factory.getDataSource());
    }

    @Test
    public void testGetUsername() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals("Expected username to match", connectionConfig.getUsername(), factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals("Expected password to match", connectionConfig.getPassword(), factory.getPassword());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        OBConsoleDataSourceFactory factoryClone = factory.deepCopy();

        assertNotSame("Expected Object references to NOT be the same", factory, factoryClone);
        assertEquals("Expected clone to have the same username", factory.getUsername(), factoryClone.getUsername());
        assertEquals("Expected clone to have the same password", factory.getPassword(), factoryClone.getPassword());
    }

    @Test
    public void testResetUsername() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newUsername";
        factory.resetUsername(mapper);

        assertEquals("Expected username to be updated", "newUsername", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newPassword";
        factory.resetPassword(mapper);

        assertEquals("Expected password to be updated", "newPassword", factory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);

        assertEquals("Expected DialectType to be MYSQL", DialectType.MYSQL, factory.getDialectType());
    }
}

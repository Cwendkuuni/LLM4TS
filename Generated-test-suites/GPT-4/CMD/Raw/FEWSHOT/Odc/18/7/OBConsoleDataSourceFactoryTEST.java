
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OBConsoleDataSourceFactoryTest {

    @InjectMocks
    OBConsoleDataSourceFactory factory;

    @Mock
    ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUsernameGetter() {
        factory.resetUsername((u) -> "newUser");
        assertEquals(factory.getUsername(), "newUser");
    }

    @Test
    public void testPasswordGetter() {
        factory.resetPassword((u) -> "newPassword");
        assertEquals(factory.getPassword(), "newPassword");
    }

    @Test
    public void testResetHost() {
        factory.resetHost(u -> "newHost");
        assertEquals(factory.host, "newHost");
    }

    @Test
    public void testResetPort() {
        factory.resetPort(u -> 1234);
        assertEquals(factory.port.intValue(), 1234);
    }

    @Test
    public void testResetSchema() {
        factory.resetSchema(u -> "newSchema");
        assertEquals(factory.defaultSchema, "newSchema");
    }

    // Add more tests accordingly based on various scenarios of your operations
}

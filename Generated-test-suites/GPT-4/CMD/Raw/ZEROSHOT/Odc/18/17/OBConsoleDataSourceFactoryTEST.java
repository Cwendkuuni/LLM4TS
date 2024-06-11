
package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig testConfig;
    private OBConsoleDataSourceFactory factory;

    @Before
    public void setUp() {
        testConfig = new ConnectionConfig();
        testConfig.setUsername("test");
        testConfig.setPassword("password");
        testConfig.setHost("localhost");
        testConfig.setPort(3306);
        testConfig.setDefaultSchema("testSchema");

        factory = new OBConsoleDataSourceFactory(testConfig, true);
    }

    @Test
    public void getUsernameTest() {
        assertEquals("test", factory.getUsername());
    }

    @Test
    public void getPasswordTest() {
        assertEquals("password", factory.getPassword());
    }

    @Test
    public void getJdbcParamsTest() {
        Map<String, String> params = factory.getJdbcParams(testConfig);
        assertEquals("64000000", params.get("maxAllowedPacket"));
        assertEquals("true", params.get("allowMultiQueries"));
        assertEquals("5000", params.get("connectTimeout"));
        assertFalse(Boolean.valueOf(params.get("useSSL")));
    }

    @Test
    public void getDataSourceTest() {
        assertNotNull(factory.getDataSource());
    }

    @Test
    public void deepCopyTest() {
        OBConsoleDataSourceFactory copiedFactory = factory.deepCopy();
        assertEquals(factory.getUsername(), copiedFactory.getUsername());
        assertEquals(factory.getPassword(), copiedFactory.getPassword());
    }

    @Test
    public void resetUsernameTest() {
        factory.resetUsername(oldUsername -> "newUsername");
        assertEquals("newUsername", factory.getUsername());
    }

    @Test
    public void resetPasswordTest() {
        factory.resetPassword(oldPassword -> "newPassword");
        assertEquals("newPassword", factory.getPassword());
    }

    @Test
    public void resetHostTest() {
        factory.resetHost(oldHost -> "newHost");
        assertTrue(factory.getJdbcUrl().contains("newHost"));
    }

    @Test
    public void resetPortTest() {
        factory.resetPort(oldPort -> 3307);
        assertTrue(factory.getJdbcUrl().contains("3307"));
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void resetPortWithNegativeValueTest() {
        factory.resetPort(oldPort -> -1);
    }
}

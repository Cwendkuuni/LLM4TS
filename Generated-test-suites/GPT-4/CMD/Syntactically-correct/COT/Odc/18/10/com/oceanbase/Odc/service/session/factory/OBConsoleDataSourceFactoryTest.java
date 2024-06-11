package com.oceanbase.odc.service.session.factory;
import org.junit.*;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    OBConsoleDataSourceFactory instance;
    ConnectionConfig connectionConfig; // this object should be initialized before running the tests

    @Before
    public void setUp() {
        // initialization of connectionConfig
        instance = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetUsername() {
        assertEquals("Username mismatch", "desired_username", instance.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("Password mismatch", "desired_password", instance.getPassword());
    }

    @Test
    public void testResetUsername() {
      instance.resetUsername(value -> "new_username");
      assertEquals("Username reset failed", "new_username", instance.getUsername());
    }

    @Test
    public void testResetPassword() {
      instance.resetPassword(value -> "new_password");
      assertEquals("Password reset failed", "new_password", instance.getPassword());
    }

    @Test
    public void testResetHost() {
      instance.resetHost(value -> "new_host");
      assertEquals("Host reset failed", "new_host", instance.getHost());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPort() {
      instance.resetPort(value -> -1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullException() {
      instance = new OBConsoleDataSourceFactory(null, true, true);
    }

    // Similar test cases can be written for other methods

    @After
    public void tearDown() {
        instance = null;
        connectionConfig = null;
    }
}
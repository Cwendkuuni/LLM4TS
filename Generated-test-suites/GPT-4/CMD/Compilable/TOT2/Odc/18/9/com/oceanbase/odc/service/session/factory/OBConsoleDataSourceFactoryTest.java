package com.oceanbase.odc.service.session.factory;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {

    private static OBConsoleDataSourceFactory factory;
    private static ConnectionConfig config;

    @BeforeClass
    public static void setUp() {
        config = new ConnectionConfig();
        // set attributes of config as necessary
        factory = new OBConsoleDataSourceFactory(config, true);
    }

    @Test
    public void getUsernameTest() {
        String expected = "expectedUsername";  // replace with the expected username
        assertEquals(expected, factory.getUsername());
    }

    // ... place other tests here ...

}
package com.oceanbase.odc.service.session.factory;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig config;

    @Before
    public void setup() {
        config = mock(ConnectionConfig.class); // Mocking the ConnectionConfig
        factory = new OBConsoleDataSourceFactory(config, false);
    }

    @Test
    public void getDataSourceTest() {
        DataSource datasource = factory.getDataSource();
        assertNotNull(datasource); // Check if datasource is not null
              
        // Further validation of datasource object based on your custom rules...
    }

    @Test
    public void getUsernameTest() {
        Factory.resetUsername(new ValueMapper() {
            @Override
            public String map(String username) {
                return username + "_mapped";
            }
        });
        assertEquals("expected_username_mapped", factory.getUsername()); 
        // assuming the initial username was "expected_username" before resetting
    }

    // ... Additional Test Cases for other methods ...

}
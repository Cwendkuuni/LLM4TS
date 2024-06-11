package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connConfig;

    @Before
    public void setup() {
         connConfig = mock(ConnectionConfig.class);
         dataSourceFactory = new OBConsoleDataSourceFactory(connConfig, true);
        // add more setup here
    }

    // example test for getJdbcUrl method
    // please note that you would need to use mocking frameworks (like Mockito) to prepare your test environment.
    // I'm writing a very basic test to give an idea only

    @Test
    public void getJdbcUrlTest(){
        String expected = "jdbc:oceanbase:@//hostname:port/database";
        when(dataSourceFactory.getJdbcUrl()).thenReturn(expected);
        assertThat(dataSourceFactory.getJdbcUrl(), is(expected));
    }

    // example test for getUsername method

    @Test
    public void getUsernameTest() {
        String expected = "testUsername";
        when(dataSourceFactory.getUsername()).thenReturn(expected);
        assertEquals(expected, dataSourceFactory.getUsername());
    }

    // write your test methods for remaining methods here

}

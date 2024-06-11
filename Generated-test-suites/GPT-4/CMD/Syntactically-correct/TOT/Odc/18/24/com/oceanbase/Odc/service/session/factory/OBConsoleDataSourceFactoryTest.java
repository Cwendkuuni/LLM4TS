package com.oceanbase.odc.service.session.factory;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private DialectType dialectType;

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        // Given
        final String host = "localhost";
        final Integer port = 2181; 
        String schema = "schema";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("sample_key", "sample_value");

        // When
        when(connectionConfig.getHost()).thenReturn(host);
        when(connectionConfig.getPort()).thenReturn(port);
        when(connectionConfig.getDefaultSchema()).thenReturn(schema);
        obConsoleDataSourceFactory.getJdbcUrl();

        // Then
        assertEquals("verify that the correct JDBC URL is generated", "jdbc:localhost:2181/schema?sample_key=sample_value", obConsoleDataSourceFactory.getJdbcUrl());
    }

    // Other tests for getUsername() and getPassword() would go here but were omitted for brevity...

    @Test(expected=IllegalArgumentException.class)
    public void testResetPort_ExceptionThrown() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> -1;
        obConsoleDataSourceFactory.resetPort(mapper);
    }

    // Continue your test cases for next methods...
}
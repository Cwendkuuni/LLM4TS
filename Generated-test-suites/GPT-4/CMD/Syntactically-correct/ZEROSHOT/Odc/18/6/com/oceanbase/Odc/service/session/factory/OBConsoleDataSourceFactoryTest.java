package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBConsoleDataSourceFactoryTest {

    @InjectMocks
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Mock
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test";
        when(connectionExtensionPoint.generateJdbcUrl(any(), any(), any(), any())).thenReturn(expectedJdbcUrl);

        String actualJdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "testUser";
        when(connectionConfig.getUsername()).thenReturn(expectedUsername);

        String actualUsername = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "testPassword";
        when(connectionConfig.getPassword()).thenReturn(expectedPassword);

        String actualPassword = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertFalse(jdbcParams.isEmpty());
    }

    @Test
    public void testGetDataSource() throws Exception {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertNotNull(dialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory cloneDataSourceFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(cloneDataSourceFactory);
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(input -> input + "new");
        assertEquals("testUsernew", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(input -> input + "new");
        assertEquals("testPasswordnew", obConsoleDataSourceFactory.getPassword());
    }

    //Similar tests can be written for resetHost, resetPort, resetSchema, resetParameters methods.
}

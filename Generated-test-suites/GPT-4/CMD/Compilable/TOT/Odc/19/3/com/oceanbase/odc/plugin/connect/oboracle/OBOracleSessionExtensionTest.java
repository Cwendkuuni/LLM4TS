package com.oceanbase.odc.plugin.connect.oboracle;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.sql.Connection;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBOracleSessionExtension sessionExtension;

    @Before
    public void setUp() {
        sessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        try {
            sessionExtension.killQuery(connection, "123");
        } catch (Exception e) {
            fail("Test failed with " + e.getMessage());
        }
    }

    @Test
    public void testSwitchSchema() {
        try {
            when(connection.getSchema()).thenReturn("myschema");
            sessionExtension.switchSchema(connection, "otherschema");
        } catch (Exception e) {
            fail("Test failed with " + e.getMessage());
        }
    }

    @Test
    public void testGetConnectionId() {
        try {
            String id= sessionExtension.getConnectionId(connection);
            assertNotNull("Connection Id must not be null", id);
        } catch (Exception e) {
            fail("Test failed with " + e.getMessage());
        }
    }

    @Test
    public void getCurrentSchema() {
        try {
            String schema= sessionExtension.getCurrentSchema(connection);
            assertNotNull("current schema must not be null", schema);
        } catch (Exception e) {
            fail("Test failed with " + e.getMessage());
        }
    }
}
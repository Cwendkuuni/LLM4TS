package com.oceanbase.odc.plugin.connect.oboracle;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.jdbc.OceanBaseConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    Connection mockConnection;

    @Test
    public void testKillQuery() {
        try {
            OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
            sessionExtension.killQuery(mockConnection, "mockConnectionId");
        } catch (Exception e) {
            fail("testKillQuery failed with Exception: " + e.getMessage());
        }
    }

    @Test
    public void testSwitchSchema() {
        try {
            OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
            String mockSchemaName = "newSchema";
            when(mockConnection.getCatalog()).thenReturn(mockSchemaName);
            String currentSchemaName = sessionExtension.getCurrentSchema(mockConnection);
            assertEquals(mockSchemaName, currentSchemaName);
        } catch (Exception e) {
            fail("testSwitchSchema failed with Exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetCurrentSchema() {
        try {
            OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
            String expectedSchema = "defaultSchema";
            when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(expectedSchema);
            String actualSchema = sessionExtension.getCurrentSchema(mockConnection);
            assertEquals(expectedSchema, actualSchema);
        } catch (Exception e) {
            fail("testGetCurrentSchema failed with Exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetConnectionId() {
        try {
            OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
            String expectedId = "mockConnectionId";
            when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(expectedId);
            String actualId = sessionExtension.getConnectionId(mockConnection);
            assertEquals(expectedId, actualId);
        } catch (Exception e) {
            fail("testGetConnectionId failed with Exception: " + e.getMessage());
        }
    }
}
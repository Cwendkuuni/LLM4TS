/*
 * Copyright (c) 2023 OceanBase.
 *
 * This is an example of a JUnit test class for the OBOracleSessionExtension class.
 */
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

import java.sql.Connection;

public class OBOracleSessionExtensionTest {

    // Create an instance of the class we will be testing
    private final OBOracleSessionExtension extension = new OBOracleSessionExtension();
    private final String schemaName = "testSchema";
    private final String connectionId = "testId";
    private final Connection connection = Mockito.mock(Connection.class);

    @Test
    public void testGetConnectionId() {
        String result = extension.getConnectionId(connection);
        Assert.assertEquals(result, connectionId);
    }

    @Test
    public void testGetCurrentSchema() {
        String result = extension.getCurrentSchema(connection);
        Assert.assertEquals(result, schemaName);
    }

    @Test
    public void testKillQuery() {
        try {
            extension.killQuery(connection, connectionId);
            verify(connection, atLeastOnce());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testSwitchSchema() {
        try {
            extension.switchSchema(connection, schemaName);
            verify(connection, atLeastOnce());
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
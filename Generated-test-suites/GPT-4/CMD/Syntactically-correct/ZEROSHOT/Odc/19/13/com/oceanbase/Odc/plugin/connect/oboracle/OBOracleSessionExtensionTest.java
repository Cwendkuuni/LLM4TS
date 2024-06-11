
/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Assume;
import org.junit.After;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest {
    
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchemaWhenSchemasAreNotEqual() throws SQLException {
        String currentSchema = "oldSchema";
        String newSchema = "newSchema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, newSchema);
        verify(connection)
                .update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(newSchema));
    }

    @Test
    public void testSwitchSchemaWhenSchemasAreEqual() throws SQLException {
        String currentSchema = "oldSchema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, currentSchema);
        verify(connection, never()).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "schema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(expectedSchema);
        Assume.assumeTrue(obOracleSessionExtension.getCurrentSchema(connection).equals(expectedSchema));
    }

    @Test
    public void testGetConnectionId() {
        String expectedId = "1";
        when(obOracleSessionExtension.getConnectionId(connection)).thenReturn(expectedId);
        Assume.assumeTrue(obOracleSessionExtension.getConnectionId(connection).equals(expectedId));
    }

    @After
    public void tearDown() {
        obOracleSessionExtension = null;
        connection = null;
    }
}

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author yaobin
 * @date 2023-03-23
 * @since 4.2.0
 */
public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection mocConnection;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mocConnection = mock(Connection.class);
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        String toBeSchema = "testSchema";
        // Same schema test
        when(obOracleSessionExtension.getCurrentSchema(mocConnection)).thenReturn(toBeSchema);
        obOracleSessionExtension.switchSchema(mocConnection, toBeSchema);
        // Verify it doesn't switch
        verify(mocConnection, times(0)).setSchema(any(String.class));
        reset(mocConnection);

        // Different Schema test
        when(obOracleSessionExtension.getCurrentSchema(mocConnection)).thenReturn("anotherSchema");
        obOracleSessionExtension.switchSchema(mocConnection, toBeSchema);
        // Verify it switches to the new schema
        verify(mocConnection, times(1)).setSchema(eq(toBeSchema));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        // Assuming the standard scenario works, we should check the edge scenario
        // where there's an exception and the connection is an instance of `OceanBaseConnection`
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        // Throw an exception when `JdbcOperations` method gets called.
        when(obOracleSessionExtension.getConnectionId(mocConnection))
                .thenThrow(new SQLException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(42L);
        String expected = "42";
        String actual = obOracleSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals(expected, actual);
    }

    @Test
    public void testKillQuery() {
        // Checking a regular scenario here.
        obOracleSessionExtension.killQuery(mocConnection, "123");
        verify(mocConnection, times(1)).killQuery(eq("123"));

        // Now checking a edge scenario where invalid (null) connectionId is passed
        obOracleSessionExtension.killQuery(mocConnection, null);
        verify(mocConnection, times(1)).killQuery(eq(null));
    }
}
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
package com.oceanbase.odc.plugin.connect.obmysql;

import org.mockito.Mockito;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import java.sql.Connection;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        OBMySQLSessionExtension obext = new OBMySQLSessionExtension();
        Connection mockConnection = Mockito.mock(Connection.class);
        obext.killQuery(mockConnection, "123");
        verify(mockConnection).clearWarnings();
    }

    @Test
    public void testSwitchSchema() throws Exception {
        OBMySQLSessionExtension obext = new OBMySQLSessionExtension();
        Connection mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockConnection.getCatalog()).thenReturn("schema1");
        obext.switchSchema(mockConnection, "schema1");
        verify(mockConnection, Mockito.never()).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        OBMySQLSessionExtension obext = new OBMySQLSessionExtension();
        Connection mockConnection = Mockito.mock(Connection.class);
        String returnedSchema = obext.getCurrentSchema(mockConnection);
        assertEquals(null, returnedSchema);
    }

    @Test
    public void testGetConnectionId() throws Exception {
        OBMySQLSessionExtension obext = new OBMySQLSessionExtension();
        Connection mockConnection = Mockito.mock(Connection.class);
        String returnedConnectionId = obext.getConnectionId(mockConnection);
        assertEquals(null, returnedConnectionId);
    }
}
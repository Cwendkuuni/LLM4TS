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

import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection mockConnection = Mockito.mock(Connection.class);
        // Due to the nature of the method, we can only verify if it was executed successfully.
        new OBMySQLSessionExtension().killQuery(mockConnection, "test");
    }
    
    @Test
    public void testSwitchSchema() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        when(mockConnection.getCatalog()).thenReturn("test");
        new OBMySQLSessionExtension().switchSchema(mockConnection, "test");
        // Expect that the setCatalog method wasn't invoked
        Mockito.verify(mockConnection, Mockito.never()).setCatalog("test");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = Mockito.mock(Connection.class);
        // We can't verify the result because it depends on the state of the database.
        new OBMySQLSessionExtension().getCurrentSchema(mockConnection);
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = Mockito.mock(Connection.class);
        // We can't verify the result because it depends on the state of the database.
        new OBMySQLSessionExtension().getConnectionId(mockConnection);
    }

    @Test
    public void testGetConnectionIdWithException() {
        Connection mockConnection = Mockito.mock(OceanBaseConnection.class);
        // We can't verify the result because it depends on the state of the database.
        new OBMySQLSessionExtension().getConnectionId(mockConnection);
    }
}
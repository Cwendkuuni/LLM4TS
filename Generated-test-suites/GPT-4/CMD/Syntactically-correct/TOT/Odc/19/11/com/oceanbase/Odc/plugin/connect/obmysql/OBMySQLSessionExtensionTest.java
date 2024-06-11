
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

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension extension;
    private Connection mockConnection;

    @Before
    public void setup() {
        extension = new OBMySQLSessionExtension();
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        // Assuming that this just executes. No value to check.
        extension.killQuery(mockConnection,"1234");
    }

    @Test(expected = SQLException.class)
    public void switchSchema_ThrowsSQLException() throws Exception {
        mockConnection.setCatalog("");
        extension.switchSchema(mockConnection, "");
    }

    @Test
    public void testGetCurrentSchema() {
        // This will need to be setup in a way that you can validate the query ran and returned the expected result
        String result = extension.getCurrentSchema(mockConnection);
        assertEquals(result, "TEST_SCHEMA");
    }

    @Test
    public void testGetConnectionId() {
        // This will need to be setup in a way that you can validate the query ran and returned the expected result
        extension.getConnectionId(mockConnection);
        verify(mockConnection, times(1)).getServerThreadId();
    }
}

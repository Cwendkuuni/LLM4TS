
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

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection mockConnection = Mockito.mock(Connection.class);
        String connectionId = "1234";
        
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(mockConnection, connectionId);
        
        // Add suitable assertion here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        String schemaName = "testSchema";
        
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(mockConnection, schemaName);

        // Add suitable assertion here
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = Mockito.mock(Connection.class);
        
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String result = extension.getCurrentSchema(mockConnection);

        // Add suitable assertion here
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = Mockito.mock(Connection.class);
        OceanBaseConnection obConnection = Mockito.mock(OceanBaseConnection.class);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();

        Mockito.when(mockConnection instanceof OceanBaseConnection).thenReturn(obConnection);
        obConnection.setServerThreadId(12345);
        String result = extension.getConnectionId(mockConnection);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("12345", result);
    }
}

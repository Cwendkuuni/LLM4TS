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
import java.sql.SQLException;
import static org.mockito.Mockito.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Tester
 * @date 2023-04-01
 * @since 4.2.0
 */

// These tests are basic. They need to be combined with a testing framework for the database.
// They assume that methods from JdbcOperationsUtil return the expected results.
public class OBMySQLSessionExtensionTest {
    Connection connection = mock(Connection.class);
    OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery(){
        obMySQLSessionExtension.killQuery(connection, "TEST");
        //To do - assert that the query is killed
    }

    @Test
    public void testSwitchSchema() throws SQLException{
        when(connection.getCatalog())
                .thenReturn("schema2", "schema2")
                .thenThrow(new SQLException());;
        obMySQLSessionExtension.switchSchema(connection, "schema1");
        //To do - assert that schema is switched to schema1
    }

    @Test(expected = IllegalStateException.class)
    public void testGetConnectionId_WithException(){
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        obMySQLSessionExtension.getConnectionId(connection);
        //To do - assert that connection is made with OceanBaseConnection
    }

    @Test
    public void testGetConnectionId() {
        obMySQLSessionExtension.getConnectionId(connection);
        //To do - assert that connection Id is returned
    }
}
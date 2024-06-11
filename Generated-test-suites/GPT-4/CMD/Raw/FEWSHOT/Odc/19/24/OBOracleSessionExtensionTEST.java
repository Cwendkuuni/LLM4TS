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
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock private Connection connection;
    @Mock private OceanBaseConnection oceanBaseConnection;
    @Mock private JdbcOperationsUtil mockJdbcOperationsUtil;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void init() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        when(connection instanceof OceanBaseConnection).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(123L);
        when(mockJdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn("testSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        assertEquals("testSchema", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        assertEquals("123", obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, "123");
        verify(mockJdbcOperationsUtil).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obOracleSessionExtension.switchSchema(connection, "anotherSchema");
        verify(mockJdbcOperationsUtil).update("ALTER SESSION SET CURRENT_SCHEMA=anotherSchema");
    }
}
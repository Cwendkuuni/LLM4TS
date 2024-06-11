package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

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
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @Mock
    private com.oceanbase.odc.common.util.JdbcOperationsUtil jdbcOperationsUtil;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws Exception {
        String connectionId = "test";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test";
        when(connection.getCatalog()).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(0)).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("test");
        obMySQLSessionExtension.getCurrentSchema(connection);
        verify(jdbcOperationsUtil).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("test");
        obMySQLSessionExtension.getConnectionId(connection);
        verify(jdbcOperationsUtil).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionIdException() {
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        obMySQLSessionExtension.getConnectionId(oceanBaseConnection);
        verify(oceanBaseConnection).getServerThreadId();
    }
}

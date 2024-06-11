
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
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connectionMock;

    @Mock
    private OceanBaseConnection oceanBaseConnectionMock;

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtilMock;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connectionMock, "testConnectionId");
        verify(jdbcOperationsUtilMock, times(1)).execute(anyString());
    }

    @Test
    public void testSwitchSchema() throws Exception {
        String schema = "testSchema";
        doReturn(schema).when(jdbcOperationsUtilMock).queryForObject(anyString());
        obMySQLSessionExtension.switchSchema(connectionMock, schema);
        verify(connectionMock, times(1)).setCatalog(schema);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testSchema";
        doReturn(expectedSchema).when(jdbcOperationsUtilMock).queryForObject(anyString());
        String actualSchema = obMySQLSessionExtension.getCurrentSchema(connectionMock);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "testConnectionId";
        doReturn(expectedConnectionId).when(jdbcOperationsUtilMock).queryForObject(anyString());
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connectionMock);
        assertEquals(expectedConnectionId, actualConnectionId);
        doThrow(new RuntimeException()).when(jdbcOperationsUtilMock).queryForObject(anyString());
        actualConnectionId = obMySQLSessionExtension.getConnectionId(oceanBaseConnectionMock);
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}

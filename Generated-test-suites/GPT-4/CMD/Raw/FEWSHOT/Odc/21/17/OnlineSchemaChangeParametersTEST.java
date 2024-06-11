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
package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.core.shared.model.TableIdentity;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        oscp.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(oscp.isContinueOnError());
        oscp.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(oscp.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Prepare parameters
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("H2");
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        oscp.setSqlContent("DROP TABLE Customers;");
        oscp.setDelimiter(";");
        oscp.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        
        // Test
        List<OnlineSchemaChangeScheduleTaskParameters> subtaskParams = oscp.generateSubTaskParameters(connectionConfig, "testSchema");

        // Validate
        assertEquals(1, subtaskParams.size());  // As we're providing only one SQL statement in sqlContent.
        // Add more assertions here to validate the returned subtaskParams...
    }
}
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

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import org.junit.Assert;
import java.util.List;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        
        // Test when error strategy is CONTINUE
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(params.isContinueOnError());
        
        // Test when errorStratgey is not CONTINUE
        params.setErrorStrategy(TaskErrorStrategy.STOP);
        Assert.assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters(){

        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setSqlContent("ALTER TABLE tableName ADD COLUMN columnName dataType;");
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        String schema = "test_schema";

        // Here assuming that the actual implementation of connectionConfig, sqlContent, schema
        // are correct and will pass the test, if not, you should provide the correct implementation.
        List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig, schema);
        
        Assert.assertNotNull(result);
    }
}
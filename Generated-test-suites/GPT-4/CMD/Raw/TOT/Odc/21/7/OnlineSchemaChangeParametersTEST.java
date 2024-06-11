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
package com.oceanbase.odc.service.onlineschemachange.test;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters params;

    @Before
    public void setUp() {
        params = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        // Given
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        // When & Then
        assertEquals(true, params.isContinueOnError());

        // Given
        params.setErrorStrategy(TaskErrorStrategy.STOP);

        // When & Then
        assertEquals(false, params.isContinueOnError());
    }

    // TODO: Write tests for generateSubTaskParameters
    // It's difficult to write these tests as we don't have access to "ConnectionConfig", "OnlineSchemaChangeSqlType", or "OnlineSchemaChangeScheduleTaskParameters" and how they behave is not defined.
}
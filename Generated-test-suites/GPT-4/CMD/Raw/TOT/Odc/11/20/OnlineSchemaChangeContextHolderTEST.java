
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

package com.oceanbase.odc.service.onlineschemachange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeContextHolderTest {

    @BeforeEach
    public void setup() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLongInput() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        Assert.assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTraceStringInput() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        Assert.assertEquals("123", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("456", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
    
    @Test(expected = NumberFormatException.class)
    public void testTraceStringInput_invalidData() {
        OnlineSchemaChangeContextHolder.trace("abc", "def", "ghi");
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        OnlineSchemaChangeContextHolder.clear();
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }
    
    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("testKey", "testValue");
        OnlineSchemaChangeContextHolder.retrace(context);
        Assert.assertEquals("testValue", OnlineSchemaChangeContextHolder.get("testKey"));
    }
}

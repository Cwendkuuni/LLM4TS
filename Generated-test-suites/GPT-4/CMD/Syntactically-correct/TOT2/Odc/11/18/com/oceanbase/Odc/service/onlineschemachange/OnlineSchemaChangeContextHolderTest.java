
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

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.Mockito;
import org.slf4j.MDC;
import java.util.Map;
import java.util.HashMap;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceLongParameters() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringParameters() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceNullParameters() {
        OnlineSchemaChangeContextHolder.trace((Long) null, null, null);
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        OnlineSchemaChangeContextHolder.retrace(map);
        assertEquals("value1", MDC.get("key1"));
        assertEquals("value2", MDC.get("key2"));
    }

    @Test
    public void testClear() {
        MDC.put("key", "value");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("key"));
    }

    @Test
    public void testGet() {
        MDC.put("key", "value");
        String result = OnlineSchemaChangeContextHolder.get("key");
        assertEquals("value", result);
    }
}

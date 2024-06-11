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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> context;

    @Before
    public void setUp() {
        this.context = new HashMap<>();
        this.context.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");
        this.context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        this.context.put(OdcConstants.ORGANIZATION_ID, "1");
    }

    @Test
    public void testTrace_withLongs() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        assertEquals(context, MDC.getCopyOfContextMap());
    }
    
    @Test
    public void testTrace_withStrings() {
        OnlineSchemaChangeContextHolder.trace("1", "1", "1");
        assertEquals(context, MDC.getCopyOfContextMap());
    }
    
    @Test
    public void testTrace_withNullStrings() {
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "null");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "null");
        context.put(OdcConstants.ORGANIZATION_ID, "null");
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        
        // should not be equal, as nulls will be converted to "null"
        assertNotEquals(context, MDC.getCopyOfContextMap());
    }

    @Test(expected = NumberFormatException.class)
    public void testTrace_withInvalidStrings() {
        OnlineSchemaChangeContextHolder.trace("abc", "def", "ghi");
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(context, MDC.getCopyOfContextMap());
    }
    
    @Test
    public void testClear() {
        MDC.setContextMap(context);
        OnlineSchemaChangeContextHolder.clear();
        assertTrue(MDC.getCopyOfContextMap().isEmpty());
    }

    @Test
    public void testGet() {
        MDC.setContextMap(context);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

}
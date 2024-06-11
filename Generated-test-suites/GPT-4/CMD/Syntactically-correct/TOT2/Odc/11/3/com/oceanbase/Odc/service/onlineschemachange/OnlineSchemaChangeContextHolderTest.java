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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() throws Exception {
        MDC.clear();
    }

    @After
    public void tearDown() throws Exception {
        MDC.clear();
    }

    @Test
    public void testTraceWithLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);	
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // other assertions goes here
    }

    @Test
    public void testTraceWithString() {
        OnlineSchemaChangeContextHolder.trace("2", "2", "2");	
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        // other assertions goes here
    }

    @Test
    public void testRetrace() {
        // Setup the context map
        Map<String, String> originalContext = MDC.getCopyOfContextMap();
        MDC.put("Test key", "Test value");
        OnlineSchemaChangeContextHolder.retrace(originalContext);

        assertNull(MDC.get("Test key"));
        // other assertions goes here
    }

    @Test
    public void testClear() {
        MDC.put("Test key", "Test value");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("Test key"));
    }

    @Test
    public void testGet() {
        MDC.put("Test key", "Test value");
        assertEquals("Test value", OnlineSchemaChangeContextHolder.get("Test key"));
    }
}

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

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTraceLongs() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);

        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), is("456"));
        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), is("123"));
        assertThat(MDC.get(OdcConstants.ORGANIZATION_ID), is("789"));
    }

    @Test
    public void testTraceStrings() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");

        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), is("456"));
        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), is("123"));
        assertThat(MDC.get(OdcConstants.ORGANIZATION_ID), is("789"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "111");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "222");
        context.put(OdcConstants.ORGANIZATION_ID, "333");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), is("111"));
        assertThat(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), is("222"));
        assertThat(MDC.get(OdcConstants.ORGANIZATION_ID), is("333"));
    }
    
    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.getCopyOfContextMap());
    }
    
    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertThat(taskId, is("456"));
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

}

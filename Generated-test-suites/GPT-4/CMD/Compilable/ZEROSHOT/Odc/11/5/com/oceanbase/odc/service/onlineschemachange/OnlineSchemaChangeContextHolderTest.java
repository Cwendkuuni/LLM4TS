
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {
    
    private final Long userId = 123L;
    private final Long taskId = 456L;
    private final Long organizationId = 789L;

    @Mock
    SecurityContextUtils securityContextUtils;

    @Before
    public void setup() {
        MDC.clear();  // Ensure MDC starts from a clean state
    }

    @After
    public void teardown() {
        MDC.clear();  // Ensure MDC is cleared after each test
    }

    @Test
    public void testTraceWithLongParameters() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID)); 
    }

    @Test
    public void testTraceWithStringParameters() {
        OnlineSchemaChangeContextHolder.trace(userId.toString(), taskId.toString(), organizationId.toString());
        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("testKey", "testValue");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("testValue", MDC.get("testKey"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        String key = "customKey";
        String value = "customValue";
        MDC.put(key, value);
        assertEquals(value, OnlineSchemaChangeContextHolder.get(key));
    }
}

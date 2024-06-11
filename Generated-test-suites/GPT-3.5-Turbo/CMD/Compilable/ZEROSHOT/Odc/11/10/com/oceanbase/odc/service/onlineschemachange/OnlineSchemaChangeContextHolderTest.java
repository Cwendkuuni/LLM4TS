package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

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
    public void testTrace_Long() {
        OnlineSchemaChangeContextHolder.trace(1L, 123L, 456L);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("456", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTrace_String() {
        OnlineSchemaChangeContextHolder.trace("1", "123", "456");
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("456", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");
        context.put(OdcConstants.ORGANIZATION_ID, "456");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("456", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "123");
        MDC.put(OdcConstants.ORGANIZATION_ID, "456");

        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get("nonExistentKey"));
    }
}
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.slf4j.MDC;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTraceLongs() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);

        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }


    @Test
    public void testTraceStrings() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");

        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }


    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("new_key", "new_value");
        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("new_value", MDC.get("new_key"));
    }

    @Test
    public void testClear() {
        MDC.put("key_to_be_cleared", "value");
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get("key_to_be_cleared"));
    }

    @Test
    public void testGet() {
        MDC.put("test_key", "test_value");
        assertEquals("test_value", OnlineSchemaChangeContextHolder.get("test_key"));
    }

    @Test
    public void testGetNonExisting() {
        assertNull(OnlineSchemaChangeContextHolder.get("non_existing_key"));
    }
}
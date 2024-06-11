package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLong() {
        Long userId = 123L;
        Long taskId = 124L;
        Long organizationId = 125L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), userId.toString());
        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), taskId.toString());
        assertEquals(MDC.get(OdcConstants.ORGANIZATION_ID), organizationId.toString());
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringNumberFormatException() {
        OnlineSchemaChangeContextHolder.trace("someString", "somethingElse", "123");
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("123", "124", "125");

        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), "123");
        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), "124");
        assertEquals(MDC.get(OdcConstants.ORGANIZATION_ID), "125");
    }

    @Test(expected = NullPointerException.class)
    public void testRetraceNullException() {
        OnlineSchemaChangeContextHolder.retrace(null);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key", "value");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(MDC.get("key"), "value");
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

        assertEquals(OnlineSchemaChangeContextHolder.get("key"), "value");

        assertNull(OnlineSchemaChangeContextHolder.get("notExistingKey"));
    }
}
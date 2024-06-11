package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import static org.junit.Assert.assertEquals;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        MDC.clear();
    }

    @Test
    public void testTrace_Long() {
        OnlineSchemaChangeContextHolder.trace(1L, 10L, 100L);

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("10", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("100", MDC.get("organizationId"));
    }

    @Test
    public void testTrace_String() {
        OnlineSchemaChangeContextHolder.trace("1", "10", "100");

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("10", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("100", MDC.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.trace(1L, 10L, 100L);
        MDC.clear();

        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, MDC.get("organizationId"));

        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("10", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("100", MDC.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 10L, 100L);
        OnlineSchemaChangeContextHolder.clear();

        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, MDC.get("organizationId"));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 10L, 100L);

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("10", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("100", OnlineSchemaChangeContextHolder.get("organizationId"));
    }
}
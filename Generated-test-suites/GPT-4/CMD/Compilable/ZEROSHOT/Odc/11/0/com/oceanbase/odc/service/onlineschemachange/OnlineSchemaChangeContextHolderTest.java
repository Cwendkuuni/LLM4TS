package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        MDC.clear();
    }

    @Test
    public void trace_withLongParameters_setsContextCorrectly() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("The logged user id doesn't match with the user id used in trace", "1", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("The logged task id doesn't match with the task id used in trace", "2", MDC.get("taskId"));
        assertEquals("The logged organization id doesn't match with the organization id used in trace", "3", MDC.get("organizationId"));
    }

    @Test
    public void trace_withStringParameters_setContextCorrectly() {
        OnlineSchemaChangeContextHolder.trace("4", "5", "6");
        assertEquals("The logged user id doesn't match with the user id used in trace", "4", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("The logged task id doesn't match with the task id used in trace", "5", MDC.get("taskId"));
        assertEquals("The logged organization id doesn't match with the organization id used in trace", "6", MDC.get("organizationId"));
    }

    @Test
    public void retrace_setsContextCorrectly() {
        Map<String, String> context = new HashMap<>();
        context.put("onlineSchemaChangeWorkSpace", "7");
        context.put("taskId", "8");
        context.put("organizationId", "9");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("The retrieved user id doesn't match with the user id used in retrace", "7", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("The retrieved task id doesn't match with the task id used in retrace", "8", MDC.get("taskId"));
        assertEquals("The retrieved organization id doesn't match with the organization id used in retrace", "9", MDC.get("organizationId"));
    }

    @Test
    public void clear_removesContextCorrectly() {
        OnlineSchemaChangeContextHolder.trace("10", "11", "12");

        // ensure context is populated
        assertEquals("10", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("11", MDC.get("taskId"));
        assertEquals("12", MDC.get("organizationId"));

        // now execute clear and validate context is empty
        OnlineSchemaChangeContextHolder.clear();
        assertNull("The user id was not removed properly", MDC.get("onlineSchemaChangeWorkSpace"));
        assertNull("The task id was not removed properly", MDC.get("taskId"));
        assertNull("The organization id was not removed properly", MDC.get("organizationId"));
    }

    @Test
    public void get_retrievesContextValueCorrectly() {
        MDC.put("taskWorkSpace", "13");
        MDC.put("taskId", "14");
        MDC.put("organizationId","15");
        assertEquals("The retrieved user id doesn't match with the user id used in get", "13", OnlineSchemaChangeContextHolder.get("taskWorkSpace"));
        assertEquals("The retrieved task id doesn't match with the task id used in get", "14", OnlineSchemaChangeContextHolder.get("taskId"));
        assertEquals("The retrieved organization id doesn't match with the organization id used in get", "15", OnlineSchemaChangeContextHolder.get("organizationId"));
    }
}

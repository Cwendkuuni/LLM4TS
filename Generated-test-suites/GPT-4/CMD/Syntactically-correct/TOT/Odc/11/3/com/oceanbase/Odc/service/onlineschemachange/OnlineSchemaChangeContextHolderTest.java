package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() throws Exception {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTrace_withLongs() {
        Long taskID = 2L;
        Long userID = 4L;
        Long organizationID = 8L;

        OnlineSchemaChangeContextHolder.trace(userID, taskID, organizationID);

        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("4", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("8", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTrace_withStrings() {
        String taskID = "2";
        String userID = "4";
        String organizationID = "8";

        OnlineSchemaChangeContextHolder.trace(userID, taskID, organizationID);

        assertEquals(taskID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(userID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(organizationID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }
    
    @Test(expected=NumberFormatException.class)
    public void testTrace_withString_NonNumberValue() {
        String taskID = "2";
        String userID = "nonNumberValue";
        String organizationID = "8";

        OnlineSchemaChangeContextHolder.trace(userID, taskID, organizationID);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "12");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "15");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("12", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("15", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "3");

        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "3");

        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }
}

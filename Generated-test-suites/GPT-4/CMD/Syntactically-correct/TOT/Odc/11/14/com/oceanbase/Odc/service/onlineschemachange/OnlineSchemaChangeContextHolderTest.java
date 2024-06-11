package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    private static final String TEST_ID = "1";
    private static final String TEST_USER = "testUser";
    private static final String TEST_ORGANIZATION = "testOrg";

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void trace_withLongs_setsMdcAndUser() {
        OnlineSchemaChangeContextHolder.trace(Long.parseLong(TEST_USER), Long.parseLong(TEST_ID), Long.parseLong(TEST_ORGANIZATION));

        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), TEST_ID);
        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), TEST_USER);
        assertEquals(MDC.get(OdcConstants.ORGANIZATION_ID), TEST_ORGANIZATION);
    }

    @Test
    public void trace_withStrings_setsMdcAndUser() {
        OnlineSchemaChangeContextHolder.trace(TEST_USER, TEST_ID, TEST_ORGANIZATION);

        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), TEST_ID);
        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), TEST_USER);
        assertEquals(MDC.get(OdcConstants.ORGANIZATION_ID), TEST_ORGANIZATION);
    }

    @Test
    public void retrace_setsContextMap() {
        Map<String, String> context = new HashMap<>();
        context.put("key", "value");
        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(MDC.get("key"), "value");
    }

    @Test
    public void clear_clearsMdc() {
        MDC.put("key", "value");
        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get("key"));
    }

    @Test
    public void get_returnsValueFromMdc() {
        MDC.put("key", "value");

        String result = OnlineSchemaChangeContextHolder.get("key");

        assertEquals(result, "value");
    }    
    
    @Test(expected = NumberFormatException.class)
    public void trace_withInvalidStringIds_throwsNumberFormatException(){
        OnlineSchemaChangeContextHolder.trace("InvalidLong", "InvalidLong", "InvalidLong");
    }
}
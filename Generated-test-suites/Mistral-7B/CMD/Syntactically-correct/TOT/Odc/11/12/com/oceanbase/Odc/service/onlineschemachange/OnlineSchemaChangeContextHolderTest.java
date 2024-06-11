package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private static final Long USER_ID = 1L;
    private static final Long TASK_ID = 1L;
    private static final Long ORGANIZATION_ID = 1L;
    private static final String USER_ID_STR = "1";
    private static final String TASK_ID_STR = "1";
    private static final String ORGANIZATION_ID_STR = "1";
    private static final Map<String, String> CONTEXT = new HashMap<String, String>() {{
        put(TASK_WORK_SPACE, USER_ID_STR);
        put(TASK_ID, TASK_ID_STR);
        put(OdcConstants.ORGANIZATION_ID, ORGANIZATION_ID_STR);
    }};

    private OnlineSchemaChangeContextHolder classUnderTest;

    @Before
    public void setup() {
        classUnderTest = new OnlineSchemaChangeContextHolder();
        MDC.putLocalMap(MDC.KEY_LOCAL_MAP, new HashMap<>());
    }

    @Test
    public void testTraceWithLongParameters() {
        classUnderTest.trace(USER_ID, TASK_ID, ORGANIZATION_ID);
        assertEquals( Arrays.asList(USER_ID_STR, TASK_ID_STR, ORGANIZATION_ID_STR), getAllValues() );
        classUnderTest.clear();
        assertNull( get(TASK_WORK_SPACE) );
        assertNull( get(TASK_ID) );
        assertNull( get(OdcConstants.ORGANIZATION_ID) );
    }

    @Test
    public void testTraceWithStringParameters() {
        when(SecurityContextUtils.parseLongFromString(userId)).thenReturn(USER_ID);
        when(SecurityContextUtils.parseLongFromString(taskId)).thenReturn(TASK_ID);
        when(SecurityContextUtils.parseLongFromString(organizationId)).thenReturn(ORGANIZATION_ID);

        SecurityContextUtils securityContextUtils = mock(SecurityContextUtils.class);
        OnlineSchemaChangeContextHolder.trace(securityContextUtils, USER_ID_STR, TASK_ID_STR, ORGANIZATION_ID_STR);

        assertEquals( Arrays.asList(USER_ID_STR, TASK_ID_STR, ORGANIZATION_ID_STR), getAllValues() );
        OnlineSchemaChangeContextHolder.clear();
        assertNull( get(TASK_WORK_SPACE) );
        assertNull( get(TASK_ID) );
        assertNull( get(OdcConstants.ORGANIZATION_ID) );
    }

    @Test
    public void testRetrace() {
        classUnderTest.trace(USER_ID, TASK_ID, ORGANIZATION_ID);
        OnlineSchemaChangeContextHolder.retrace(CONTEXT);
        assertEquals(CONTEXT, getAllValues());
        OnlineSchemaChangeContextHolder.clear();
        assertNull( get(TASK_WORK_SPACE) );
        assertNull( get(TASK_ID) );
        assertNull( get(OdcConstants.ORGANIZATION_ID) );
    }

    private Map<String, String> getAllValues() {
        return (Map<String, String>) MDC.get(MDC.KEY_LOCAL_MAP);
    }
}
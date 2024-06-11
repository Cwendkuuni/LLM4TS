package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> context;

    @Before
    public void setUp() {
        context = new HashMap<>();
        MDC.setContextMap(context);
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongParameters() {
        long userId = 1L;
        long taskId = 2L;
        long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(String.valueOf(userId), OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals(String.valueOf(taskId), OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals(String.valueOf(organizationId), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        // test with null values
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        // test with null values
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        MDC.put(OdcConstants.ORGANIZATION_ID, "testOrgId");
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        Map<String, String> contextBeforeRetrace = MDC.getCopyOfContextMap();

        OnlineSchemaChangeContextHolder.retrace(context);

        Map<String, String> contextAfterRetrace = MDC.getCopyOfContextMap();

        assertEquals(contextBeforeRetrace, contextAfterRetrace);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();

        assertEquals(null, OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        // test with empty context map
        OnlineSchemaChangeContextHolder.retrace(new HashMap<>());
        OnlineSchemaChangeContextHolder.clear();
    }
}
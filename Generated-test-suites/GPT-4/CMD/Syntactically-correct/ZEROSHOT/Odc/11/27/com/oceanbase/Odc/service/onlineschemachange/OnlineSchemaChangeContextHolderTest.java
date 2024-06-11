
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.slf4j.MDC;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeContextHolderTest {

    private final Long userId = 123456L;
    private final Long taskId = 654321L;
    private final Long organizationId = 111222L;
    private final String userIdS = "123456";
    private final String taskIdS = "654321";
    private final String organizationIdS = "111222";

    @Before
    public void setUp() throws Exception {
        Mockito.mock(SecurityContextUtils.class);
    }

    @After
    public void tearDown() throws Exception {
        MDC.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String taskSpace = MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals(userIdS, taskSpace);

        String tid = MDC.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskIdS, tid);

        String orgId = MDC.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(organizationIdS, orgId);

        Mockito.verify(SecurityContextUtils.class, times(1)).setCurrentUser(userId, organizationId, null);
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace(userIdS, taskIdS, organizationIdS);

        String taskSpace = MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals(userIdS, taskSpace);

        String tid = MDC.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskIdS, tid);

        String orgId = MDC.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(organizationIdS, orgId);

        Mockito.verify(SecurityContextUtils.class, times(1)).setCurrentUser(userId, organizationId, null);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userIdS);
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, taskIdS);
        context.put(OdcConstants.ORGANIZATION_ID, organizationIdS);
        OnlineSchemaChangeContextHolder.retrace(context);

        String taskSpace = MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals(userIdS, taskSpace);

        String tid = MDC.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskIdS, tid);

        String orgId = MDC.get(OdcConstants.ORGANIZATION_ID);
        assertEquals(organizationIdS, orgId);
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userIdS, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdS, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdS, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}

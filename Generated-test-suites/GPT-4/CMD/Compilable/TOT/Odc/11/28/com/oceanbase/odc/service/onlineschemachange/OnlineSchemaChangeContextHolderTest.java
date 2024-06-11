package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.core.shared.constant.OdcConstants;

public class OnlineSchemaChangeContextHolderTest {

    private final String userId = "1";
    private final Long userIdL = 1L;

    private final String taskID = "100";
    private final Long taskIDL = 100L;
    
    private final String organizationId = "500";
    private final Long organizationIdL = 500L;
    
    private Map<String, String> context;

    @Before
    public void setup() {
        context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, taskID);
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId);
        context.put(OdcConstants.ORGANIZATION_ID, organizationId);
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(userIdL, taskIDL, organizationIdL);
        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace(userId, taskID, organizationId);
        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        MDC.setContextMap(context);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.setContextMap(context);
        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @After
    public void cleanup() {
        MDC.clear();
    }
}
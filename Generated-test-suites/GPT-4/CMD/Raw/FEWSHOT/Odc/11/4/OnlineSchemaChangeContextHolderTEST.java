package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {
    Map<String, String> context;

    @Before
    public void setup() {
        context = new HashMap<String, String>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");
    }

    @After
    public void clearContext() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLongParameters() {
        OnlineSchemaChangeContextHolder.trace(getUserIdLong(), getTaskIdLong(), getOrganizationIdLong());

        assertEquals(getUserId(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(getTaskId(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(getOrganizationId(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringParameters() {
        OnlineSchemaChangeContextHolder.trace(getUserId(), getTaskId(), getOrganizationId());

        assertEquals(getUserId(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(getTaskId(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(getOrganizationId(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(getUserId(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(getTaskId(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(getOrganizationId(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.retrace(context);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    private String getUserId() {
        return context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
    }

    private String getTaskId() {
        return context.get(OnlineSchemaChangeContextHolder.TASK_ID);
    }

    private String getOrganizationId() {
        return context.get(OdcConstants.ORGANIZATION_ID);
    }

    private Long getUserIdLong() {
        return Long.parseLong(getUserId());
    }

    private Long getTaskIdLong() {
        return Long.parseLong(getTaskId());
    }

    private Long getOrganizationIdLong() {
        return Long.parseLong(getOrganizationId());
    }
}
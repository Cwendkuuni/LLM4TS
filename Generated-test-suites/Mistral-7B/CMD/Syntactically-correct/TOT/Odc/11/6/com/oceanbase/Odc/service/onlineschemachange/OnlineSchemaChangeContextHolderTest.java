package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;
    private Long userId;
    private Long organizationId;
    private Long taskId;
    private Map<String, String> context;

    @Before
    public void setup() {
        onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
        userId = 1L;
        organizationId = 2L;
        taskId = 3L;
        context = new HashMap<>();
    }

    @Test
    public void testTraceWithValidArguments() {
        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assertEquals(userId + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        verify(SecurityContextUtils, times(1)).setCurrentUser(userId, organizationId, null);
    }

    @Test
    public void testTraceWithNullArguments() {
        onlineSchemaChangeContextHolder.trace(null, null, null);
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        verify(SecurityContextUtils, never()).setCurrentUser(anyLong(), anyLong(), any());
    }

    @Test
    public void testTraceWithStringArguments() {
        onlineSchemaChangeContextHolder.trace(userId + "", taskId + "", organizationId + "");
        assertEquals(userId + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId + "", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId + "", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        verify(SecurityContextUtils, times(1)).setCurrentUser(userId, organizationId, null);
    }

    @Test
    public void testReTrace() {
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");
        onlineSchemaChangeContextHolder.retrace(context);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        onlineSchemaChangeContextHolder.clear();
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}
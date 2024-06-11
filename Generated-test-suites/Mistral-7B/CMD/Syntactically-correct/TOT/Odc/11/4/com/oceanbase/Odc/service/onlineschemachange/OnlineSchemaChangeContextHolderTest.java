package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

public class OnlineSchemaChangeContextHolderTest {

    private Map<String, String> context;

    @Before
    public void setUp() {
        context = new HashMap<String, String>();
        SecurityContextUtils securityContextUtilsMock = Mockito.mock(SecurityContextUtils.class);
        when(securityContextUtilsMock.getCurrentUser(anyLong(), anyLong(), anyString())).thenReturn(null);
        OnlineSchemaChangeContextHolder.setSecurityContextUtils(securityContextUtilsMock);
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clearContext();
        OnlineSchemaChangeContextHolder.setSecurityContextUtils(null);
    }

    @Test
    public void testTraceWithValidInput() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTraceWithNullUserId() {
        OnlineSchemaChangeContextHolder.trace(null, 2L, 3L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTraceWithNullTaskId() {
        OnlineSchemaChangeContextHolder.trace(1L, null, 3L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTraceWithNullOrganizationId() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, null);
    }

    @Test
    public void testTraceWithValidStringInput() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidStringUserId() {
        String userId = "abc";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidStringTaskId() {
        String userId = "1";
        String taskId = "abc";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidStringOrganizationId() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "abc";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test
    public void testRetrace() {
        Map<String, String> contextToSet = new HashMap<String, String>();
        contextToSet.put("key1", "value1");
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(contextToSet);

        Map<String, String> currentContext = OnlineSchemaChangeContextHolder.getContext();
        assertEquals(contextToSet, currentContext);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();

        assertEquals(null, OnlineSchemaChangeContextHolder.getContext());
    }
}
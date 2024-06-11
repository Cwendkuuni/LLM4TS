package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;
import junit.framework.Assert;
import junit.runner.JUnit4;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongValues() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getContextMap();
        Assert.assertNotNull(context);
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringValues() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getContextMap();
        Assert.assertNotNull(context);
        assertEquals(userId, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, context.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithInvalidValues() {
        String userId = "abc";
        String taskId = "def";
        String organizationId = "xyz";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(context.get(OnlineSchemaChangeContextHolder.TASK_ID), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(context.get(OdcConstants.ORGANIZATION_ID), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        assertEquals(1L, Long.parseLong(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)));
        assertEquals(2L, Long.parseLong(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID)));
        assertEquals(3L, Long.parseLong(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID)));

        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testSetCurrentUser() {
        Long userId = 1L;
        Long organizationId = 2L;

        SecurityContextUtils securityContextUtilsMock = Mockito.mock(SecurityContextUtils.class);
        OnlineSchemaChangeContextHolder.trace(userId, null, organizationId);
        Mockito.verify(securityContextUtilsMock).setCurrentUser(userId, organizationId, null);
    }
}
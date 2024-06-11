package com.oceanbase.odc.service.onlineschemachange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @InjectMocks
    private OnlineSchemaChangeContextHolder contextHolder;

    @Test
    public void testTraceLongUserIdLongTaskIdLongOrganizationId() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        contextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId + "", context.get(TASK_WORK_SPACE));
        assertEquals(taskId + "", context.get(TASK_ID));
        assertEquals(organizationId + "", context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceNullUserIdLongTaskIdLongOrganizationId() {
        Long taskId = 2L;
        Long organizationId = 3L;

        contextHolder.trace(null, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(TASK_WORK_SPACE));
        assertEquals(taskId + "", context.get(TASK_ID));
        assertEquals(organizationId + "", context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceLongUserIdNullTaskIdLongOrganizationId() {
        Long userId = 1L;
        Long organizationId = 3L;

        contextHolder.trace(userId, null, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId + "", context.get(TASK_WORK_SPACE));
        assertNull(context.get(TASK_ID));
        assertEquals(organizationId + "", context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceLongUserIdLongTaskIdNullOrganizationId() {
        Long userId = 1L;
        Long taskId = 2L;

        contextHolder.trace(userId, taskId, null);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId + "", context.get(TASK_WORK_SPACE));
        assertEquals(taskId + "", context.get(TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringUserIdStringTaskIdStringOrganizationId() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        contextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);
        assertEquals(userIdL + "", context.get(TASK_WORK_SPACE));
        assertEquals(taskIdL + "", context.get(TASK_ID));
        assertEquals(organizationIdL + "", context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceNullUserIdStringTaskIdStringOrganizationId() {
        String taskId = "2";
        String organizationId = "3";

        contextHolder.trace(null, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(TASK_WORK_SPACE));
        assertEquals(taskId, context.get(TASK_ID));
        assertEquals(organizationId, context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringUserIdNullTaskIdStringOrganizationId() {
        String userId = "1";
        String organizationId = "3";

        contextHolder.trace(userId, null, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        Long userIdL = Long.parseLong(userId);
        assertEquals(userIdL + "", context.get(TASK_WORK_SPACE));
        assertNull(context.get(TASK_ID));
        assertEquals(organizationId, context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceStringUserIdStringTaskIdNullOrganizationId() {
        String userId = "1";
        String taskId = "2";

        contextHolder.trace(userId, taskId, null);

        Map<String, String> context = MDC.getCopyOfContextMap();
        Long userIdL = Long.parseLong(userId);
        assertEquals(userIdL + "", context.get(TASK_WORK_SPACE));
        assertEquals(taskId, context.get(TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringUserIdInvalidLongTaskIdStringOrganizationId() {
        String userId = "1";
        String taskId = "abc";
        String organizationId = "3";

        contextHolder.trace(userId, taskId, organizationId);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringUserIdStringTaskIdNullOrganizationId() {
        String userId = "1";
        String taskId = "2";

        contextHolder.trace(userId, taskId, null);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(TASK_WORK_SPACE, "1");
        context.put(TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        contextHolder.retrace(context);

        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        assertEquals(context.get(TASK_WORK_SPACE), mdcContext.get(TASK_WORK_SPACE));
        assertEquals(context.get(TASK_ID), mdcContext.get(TASK_ID));
        assertEquals(context.get(OdcConstants.ORGANIZATION_ID), mdcContext.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        MDC.put(TASK_WORK_SPACE, "1");
        MDC.put(TASK_ID, "2");
        MDC.put(OdcConstants.ORGANIZATION_ID, "3");

        contextHolder.clear();

        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        assertNull(mdcContext.get(TASK_WORK_SPACE));
        assertNull(mdcContext.get(TASK_ID));
        assertNull(mdcContext.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGetNullKey() {
        contextHolder.trace(1L, 2L, 3L);
        assertNull(contextHolder.get(null));
    }

    @Test
    public void testGetValidKey() {
        contextHolder.trace(1L, 2L, 3L);
        assertEquals("3", contextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}
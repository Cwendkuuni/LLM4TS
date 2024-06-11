package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongArgs() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArgs() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = MDC.getCopyOfContextMap();
        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);
        assertEquals(userIdL.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullArgs() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);
        Map<String, String> context = MDC.getCopyOfContextMap();
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(context.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(context.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testReTrace() {
        Map<String, String> context = new HashMap<>();
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(context, MDC.getCopyOfContextMap());
    }

    @Test(expected = IllegalStateException.class)
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        String key = OnlineSchemaChangeContextHolder.TASK_ID;
        assertNull(OnlineSchemaChangeContextHolder.get(key));
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        String value = OnlineSchemaChangeContextHolder.get(key);
        assertNotNull(value);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(key));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
        String key = OnlineSchemaChangeContextHolder.TASK_ID;
        String value = OnlineSchemaChangeContextHolder.get(key);
        assertEquals(1L + "", value);

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testSetCurrentUser() {
        Long userId = 1L;
        Long organizationId = 1L;

        OnlineSchemaChangeContextHolder.trace(userId, null, organizationId);
        SecurityContextUtils securityContextUtils = Mockito.mock(SecurityContextUtils.class);
        Mockito.when(securityContextUtils.getCurrentTenantIds()).thenReturn(new long[]{1L, 2L});
        OnlineSchemaChangeContextHolder.setCurrentUser(securityContextUtils);

        Map<String, String> context = MDC.getCopyOfContextMap();
        assertEquals(userId.toString(), context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(userId.toString(), context.get(OdcConstants.ORGANIZATION_ID));
    }

}
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Mock
    private SecurityContextUtils securityContextUtils;

    @InjectMocks
    private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;

    @Test
    public void testTraceLonguserIdLongtaskIdLongorganizationId() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
        assertEquals(userId, SecurityContextUtils.getCurrentUser().getId());
        assertEquals(organizationId, SecurityContextUtils.getCurrentUser().getOrganizationId());
        assertEquals(null, SecurityContextUtils.getCurrentUser().getUsername());
        Mockito.verify(securityContextUtils, Mockito.times(1)).setCurrentUser(userId, organizationId, null);
    }

    @Test
    public void testTraceStringuserIdStringtaskIdStringorganizationId() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);

        assertEquals(userIdL.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskIdL.toString(), MDC.get(OnlineSchemaChangeChangeContextHolder.TASK_ID));
        assertEquals(organizationIdL.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));

        Mockito.verify(securityContextUtils, Mockito.times(1)).setCurrentUser(userIdL, organizationIdL, null);
    }

    @Test
    public void testTraceNullParameters() {
        onlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
        assertNull(SecurityContextUtils.getCurrentUser());
        Mockito.verify(securityContextUtils, Mockito.never()).setCurrentUser(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(String.class));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        onlineSchemaChangeContextHolder.retrace(context);

        assertEquals(context, MDC.getCopyOfContextMap());
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        MDC.put(OnlineSchemaChangeChangeContextHolder.TASK_ID, "2");
        MDC.put(OdcConstants.ORGANIZATION_ID, "3");

        onlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullKey() {
        onlineSchemaChangeContextHolder.get(null);
    }

    @Test
    public void testGetValidKey() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "1");

        assertEquals("1", onlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

}
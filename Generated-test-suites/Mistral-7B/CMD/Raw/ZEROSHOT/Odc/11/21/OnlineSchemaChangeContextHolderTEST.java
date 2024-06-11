package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OnlineSchemaChangeContextHolder.class, SecurityContextUtils.class})
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.internal.parsers.*"})
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

        assertEquals(userId + "", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals(taskId + "", OnlineSchemaChangeChangeHolder.get(TASK_ID));
        assertEquals(organizationId + "", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        Mockito.verify(SecurityContextUtils.class).setCurrentUser(userId, organizationId, null);
    }

    @Test
    public void testTraceWithStringParameters() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Long userIdL = Long.parseLong(userId);
        Long taskIdL = Long.parseLong(taskId);
        Long organizationIdL = Long.parseLong(organizationId);

        assertEquals(userIdL + "", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals(taskIdL + "", OnlineSchemaChangeChangeHolder.get(TASK_ID));
        assertEquals(organizationIdL + "", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        Mockito.verify(SecurityContextUtils.class).setCurrentUser(userIdL, organizationIdL, null);
    }

    @Test
    public void testRetrace() {
        Map<String, String> originalContext = new HashMap<>();
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(originalContext);

        assertEquals(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE), originalContext.get(TASK_WORK_SPACE));
        assertEquals(OnlineSchemaChangeContextHolder.get(TASK_ID), originalContext.get(TASK_ID));
        assertEquals(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID), originalContext.get(OdcConstants.ORGANIZATION_ID));

        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}
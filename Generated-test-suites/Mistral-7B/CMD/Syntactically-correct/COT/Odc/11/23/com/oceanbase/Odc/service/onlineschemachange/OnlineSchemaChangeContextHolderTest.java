package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.MDC;

import com.google.common.collect.Maps;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OnlineSchemaChangeContextHolder.class, SecurityContextUtils.class, MDC.class})
public class OnlineSchemaChangeContextHolderTest {

    private static final Long USER_ID = 1L;
    private static final Long TASK_ID = 1L;
    private static final Long ORGANIZATION_ID = 1L;
    private static final String USER_ID_STR = "1";
    private static final String TASK_ID_STR = "1";
    private static final String ORGANIZATION_ID_STR = "1";

    @Before
    public void setUp() {
        PowerMockito.mockStatic(MDC.class);
        MDC.setDefaultMock(MDC.class);
    }

    @Test
    public void testTraceWithLongArg() {
        // given
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);

        // when
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);

        // then
        assertEquals(USER_ID_STR, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeChangeHolder.TASK_WORK_SPACE));
        assertEquals(taskId, TASK_ID_STR);
        assertEquals(ORGANIZATION_ID_STR, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArg() {
        // given
        OnlineSchemaChangeContextHolder.trace(USER_ID_STR, TASK_ID_STR, ORGANIZATION_ID_STR);

        // when
        Long userId = Long.parseLong(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Long taskId = Long.parseLong(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Long organizationId = Long.parseLong(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        // then
        assertEquals(USER_ID, userId);
        assertEquals(TASK_ID, taskId);
        assertEquals(ORGANIZATION_ID, organizationId);
    }

    @Test
    public void testRetrace() {
        // given
        Map<String, String> context = Maps.newConcurrentMap();
        OnlineSchemaChangeContextHolder.retrace(context);

        // when
        String taskId = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        String organizationId = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);

        // then
        assertNull(taskId);
        assertNull(organizationId);

        // verify
        Mockito.verify(MDC.class).setContextMap(context);
    }

    @Test
    public void testClear() {
        // given
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);

        // when
        OnlineSchemaChangeContextHolder.clear();

        // then
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }
}
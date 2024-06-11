package com.oceanbase.odc.service.onlineschemachange;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    private static final String USER_ID = "1";
    private static final String TASK_ID = "task1";
    private static final String ORGANIZATION_ID = "org1";

    private AtomicLong userIdCounter = new AtomicLong(Long.parseLong(USER_ID));
    private AtomicLong taskIdCounter = new AtomicLong(Long.parseLong(TASK_ID));
    private AtomicLong organizationIdCounter = new AtomicLong(Long.parseLong(ORGANIZATION_ID));

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongArguments() {
        Long userId = userIdCounter.getAndIncrement();
        Long taskId = taskIdCounter.getAndIncrement();
        Long organizationId = organizationIdCounter.getAndIncrement();
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> expectedContext = Maps.newHashMap();
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId.toString());
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId.toString());
        expectedContext.put(OdcConstants.ORGANIZATION_ID, organizationId.toString());

        OnlineSchemaChangeContextHolder.retrace(expectedContext);

        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArguments() {
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);

        Map<String, String> expectedContext = Maps.newHashMap();
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, USER_ID);
        expectedContext.put(OnlineSchemaChangeContextHolder.TASK_ID, TASK_ID);
        expectedContext.put(OdcConstants.ORGANIZATION_ID, ORGANIZATION_ID);

        OnlineSchemaChangeContextHolder.retrace(expectedContext);

        assertEquals(USER_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TASK_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(ORGANIZATION_ID, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}
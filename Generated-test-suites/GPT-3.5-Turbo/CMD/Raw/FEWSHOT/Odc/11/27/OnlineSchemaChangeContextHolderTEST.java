package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.oceanbase.odc.common.utils.TestDataGenerator;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace_Long() {
        Long userId = TestDataGenerator.generateRandomLong();
        Long taskId = TestDataGenerator.generateRandomLong();
        Long organizationId = TestDataGenerator.generateRandomLong();

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(String.valueOf(userId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(String.valueOf(taskId), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(String.valueOf(organizationId), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTrace_String() {
        String userId = TestDataGenerator.generateRandomString();
        String taskId = TestDataGenerator.generateRandomString();
        String organizationId = TestDataGenerator.generateRandomString();

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testRetrace() {
        String userId = TestDataGenerator.generateRandomString();
        String taskId = TestDataGenerator.generateRandomString();
        String organizationId = TestDataGenerator.generateRandomString();

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());

        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testClear() {
        String userId = TestDataGenerator.generateRandomString();
        String taskId = TestDataGenerator.generateRandomString();
        String organizationId = TestDataGenerator.generateRandomString();

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

}
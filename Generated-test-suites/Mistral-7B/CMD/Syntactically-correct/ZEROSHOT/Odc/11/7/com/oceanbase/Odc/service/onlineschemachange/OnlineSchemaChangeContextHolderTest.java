package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

/**
 * OnlineSchemaChangeContextHolderTest class file.
 *
 * @author Oceanbase
 */
public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        // Clear MDC context before each test
        MDC.clear();
    }

    @After
    public void tearDown() {
        // Clear MDC context after each test
        MDC.clear();
    }

    /**
     * Test setTaskId method.
     */
    @Test
    public void testSetTaskId() {
        Long userId = 1L;
        Long taskId = 10L;
        Long organizationId = 20L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskId.toString(), taskIdValue);
    }

    /**
     * Test setTaskWorkspace method.
     */
    @Test
    public void testSetTaskWorkspace() {
        Long userId = 1L;
        Long taskId = 10L;
        Long organizationId = 20L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String userIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals(userId.toString(), userIdValue);
    }

    /**
     * Test setOrganizationId method.
     */
    @Test
    public void testSetOrganizationId() {
        Long userId = 1L;
        Long taskId = 10L;
        Long organizationId = 20L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String organizationIdValue = OnlineSchemaChangeContextHolder.get(OndcConstants.ORGANIZATION_ID);
        assertEquals(organizationId.toString(), organizationIdValue);
    }

    /**
     * Test setCurrentUser method.
     */
    @Test
    public void testSetCurrentUser() {
        Long userId = 1L;
        Long taskId = 10L;
        Long organizationId = 20L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        // verify current user is set correctly
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        assertEquals(userId, currentUserId);
    }

    /**
     * Test get method with valid key.
     */
    @Test
    public void testGet() {
        Long userId = 1L;
        Long taskId = 10L;
        Long organizationId = 20L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals(taskId.toString(), taskIdValue);
    }

    /**
     * Test get method with invalid key.
     */
    @Test
    public void testGetInvalidKey() {
        Long userId = 1L;
        Long taskId = 10L;
        Long organizationId = 20L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        String invalidKeyValue = OnlineSchemaChangeContextHolder.get("invalidKey");
        assertNull(invalidKeyValue);
    }

    /**
     * Test trace with String inputs.
     */
    @Test
    public void testTraceWithStringInputs() {
        OnlineSchemaChangeContextHolder.trace("1", "10", "20");

        String userIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals("1", userIdValue);

        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals("10", taskIdValue);

        String organizationIdValue = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals("20", organizationIdValue);
    }

    /**
     * Test retrace method.
     */
    @Test
    public void testRetrace() {
        Long userId = 1L;
        Long taskId = 10L;
        Long organizationId = 20L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "11");
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "111");
        context.put(OdcConstants.ORGANIZATION_ID, "222");
        OnlineSchemaChangeContextHolder.retrace(context);

        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertEquals("11", taskIdValue);

        String userIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals("111", userIdValue);

        String organizationIdValue = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertEquals("222", organizationIdValue);
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 10L;
        Long organizationId = 20L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        OnlineSchemaChangeContextHolder.clear();

        String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
        assertNull(taskIdValue);

        String userIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertNull(userIdValue);

        String organizationIdValue = OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID);
        assertNull(organizationIdValue);
    }
}
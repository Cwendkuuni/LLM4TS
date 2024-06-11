package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    private static final String TASK_ID = "taskId";
    private static final String TASK_WORK_SPACE = "onlineSchemaChangeWorkSpace";

    private static final String TEST_USER_ID = "1";
    private static final String TEST_TASK_ID = "1";
    private static final String TEST_ORGANIZATION_ID = "1";

    @Before
    public void setUp() {
        MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceWithLongParams() {
        OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);

        assertEquals("1", MDC.get(TASK_WORK_SPACE));
        assertEquals("1", MDC.get(TASK_ID));
        assertEquals("1", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringParams() {
        OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORGANIZATION_ID);

        assertEquals(TEST_USER_ID, MDC.get(TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, MDC.get(TASK_ID));
        assertEquals(TEST_ORGANIZATION_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(TASK_WORK_SPACE, TEST_USER_ID);
        context.put(TASK_ID, TEST_TASK_ID);
        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(TEST_USER_ID, MDC.get(TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, MDC.get(TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));

    }

    @Test
    public void testClear() {
        MDC.put(TASK_ID, TEST_TASK_ID);
        assertNotNull(MDC.get(TASK_ID));

        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(TASK_ID));
    }

    @Test
    public void testGet() {
        MDC.put(TASK_ID, TEST_TASK_ID);

        assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(TASK_ID));
    }

}
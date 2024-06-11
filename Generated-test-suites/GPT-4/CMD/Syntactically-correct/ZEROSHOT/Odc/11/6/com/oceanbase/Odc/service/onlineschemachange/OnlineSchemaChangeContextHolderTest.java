package com.oceanbase.odc.service.onlineschemachange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    private static final String SAMPLE_USER_ID = "12345678";
    private static final String SAMPLE_TASK_ID = "24357689";
    private static final String SAMPLE_ORGANIZATION_ID = "57234689";

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceWithLongArgs() {
        OnlineSchemaChangeContextHolder.trace(
                Long.parseLong(SAMPLE_USER_ID),
                Long.parseLong(SAMPLE_TASK_ID),
                Long.parseLong(SAMPLE_ORGANIZATION_ID)
        );

        assertEquals(
                SAMPLE_USER_ID,
                OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)
        );
        assertEquals(
                SAMPLE_TASK_ID,
                OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID)
        );
        assertEquals(
                SAMPLE_ORGANIZATION_ID,
                OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID)
        );

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithStringArgs() {
        OnlineSchemaChangeContextHolder.trace(SAMPLE_USER_ID, SAMPLE_TASK_ID, SAMPLE_ORGANIZATION_ID);

        assertEquals(
                SAMPLE_USER_ID,
                OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)
        );
        assertEquals(
                SAMPLE_TASK_ID,
                OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID)
        );
        assertEquals(
                SAMPLE_ORGANIZATION_ID,
                OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID)
        );

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, SAMPLE_USER_ID);
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, SAMPLE_TASK_ID);
        context.put(OdcConstants.ORGANIZATION_ID, SAMPLE_ORGANIZATION_ID);

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals(
                SAMPLE_USER_ID,
                MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)
        );
        assertEquals(
                SAMPLE_TASK_ID,
                MDC.get(OnlineSchemaChangeContextHolder.TASK_ID)
        );
        assertEquals(
                SAMPLE_ORGANIZATION_ID,
                MDC.get(OdcConstants.ORGANIZATION_ID)
        );
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace(SAMPLE_USER_ID, SAMPLE_TASK_ID, SAMPLE_ORGANIZATION_ID);

        assertEquals(
                SAMPLE_USER_ID,
                OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)
        );
        assertEquals(
                SAMPLE_TASK_ID,
                OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID)
        );
        assertEquals(
                SAMPLE_ORGANIZATION_ID,
                OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID)
        );
    }
}

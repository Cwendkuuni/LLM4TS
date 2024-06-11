
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    private static final String TEST_USER_ID = "1";
    private static final String TEST_TASK_ID = "3";
    private static final String TEST_ORG_ID = "123";

    @Before
    public void setUp(){
        MDC.clear();
    }

    @Test
    public void testTraceLong(){
        OnlineSchemaChangeContextHolder.trace(Long.parseLong(TEST_USER_ID), Long.parseLong(TEST_TASK_ID), Long.parseLong(TEST_ORG_ID));
        assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(TEST_ORG_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString(){
        OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORG_ID);
        assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(TEST_ORG_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace(){
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, TEST_USER_ID);
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, TEST_TASK_ID);
        context.put(OdcConstants.ORGANIZATION_ID, TEST_ORG_ID);
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(TEST_USER_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(TEST_ORG_ID, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear(){
        OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORG_ID);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet(){
        OnlineSchemaChangeContextHolder.trace(TEST_USER_ID, TEST_TASK_ID, TEST_ORG_ID);
        assertEquals(TEST_USER_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(TEST_TASK_ID, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(TEST_ORG_ID, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }
}


import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.slf4j.MDC;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.core.shared.constant.OdcConstants;

public class OnlineSchemaChangeContextHolderTest {
    private String userId = "5";
    private String taskId = "10";
    private String organizationId = "15";

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }

    @Test
    public void testTrace_longParam() {
        long userIdL = Long.parseLong(userId);
        long taskIdL = Long.parseLong(taskId);
        long organizationIdL = Long.parseLong(organizationId);
        OnlineSchemaChangeContextHolder.trace(userIdL, taskIdL, organizationIdL);
        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTrace_stringParam() {
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId);
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId);
        context.put(OdcConstants.ORGANIZATION_ID, organizationId);
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals(userId, MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        assertEquals(userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }
}

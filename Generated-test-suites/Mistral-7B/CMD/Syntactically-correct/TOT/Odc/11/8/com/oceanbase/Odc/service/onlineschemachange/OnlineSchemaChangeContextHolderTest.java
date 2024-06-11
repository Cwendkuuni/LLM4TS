import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.MDC;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(org.junit.runner.JUnit4.class)
public class OnlineSchemaChangeContextHolderTest {

    private OnlineSchemaChangeContextHolder holder;
    private Map<String, String> context;

    @Before
    public void setUp() {
        holder = new OnlineSchemaChangeContextHolder();
        context = new HashMap<>();
    }

    @Test
    public void testTraceWithLongValues() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        holder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), holder.get(OdcConstants.ORGANIZATION_ID));

        // Set up mock logger for testing MDC.clear()
        MDC mdc = Mockito.mock(MDC.class);
        MDC.setMDCContextMap(mdc);
        holder.clear();
        Mockito.verify(mdc).clear();
    }

    @Test
    public void testTraceWithStringValues() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        holder.trace(userId, taskId, organizationId);

        assertEquals(userId, holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId, holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId, holder.get(OdcConstants.ORGANIZATION_ID));

        holder.clear();
        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(holder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        holder.trace(userId, taskId, organizationId);

        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId.toString());
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, taskId.toString());
        context.put(OdcConstants.ORGANIZATION_ID, organizationId.toString());
        holder.retrace(context);

        assertEquals(userId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), holder.get(OdcConstants.ORGANIZATION_ID));

        // Test clearing context after retracing
        holder.clear();
        Mockito.verify(mdc, Mockito.times(2)).remove(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        Mockito.verify(mdc, Mockito.times(2)).remove(OnlineSchemaChangeContextHolder.TASK_ID);
        Mockito.verify(mdc, Mockito.times(2)).remove(OdcConstants.ORGANIZATION_ID);
    }

    @Test
    public void testGet() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        holder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), holder.get(OdcConstants.ORGANIZATION_ID));

        holder.clear();
        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(holder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(holder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        holder.trace(userId, taskId, organizationId);

        holder.clear();

        assertEquals(Collections.emptyMap(), MDC.getCopyOfContextMap());
    }

    @After
    public void tearDown() {
        holder = null;
        context = null;
        MDC.clear();
    }
}
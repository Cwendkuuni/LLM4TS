import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

public class OnlineSchemaChangeContextHolderTest {

    private static final String TASK_ID = OnlineSchemaChangeContextHolder.TASK_ID;
    private static final String TASK_WORK_SPACE = OnlineSchemaChangeContextHolder.TASK_WORK_SPACE;

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 123L, 456L);
        
        assertEquals("1", MDC.get(TASK_WORK_SPACE));
        assertEquals("123", MDC.get(TASK_ID));
        assertEquals("456", MDC.get("organizationId"));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("1", "123", "456");
        
        assertEquals("1", MDC.get(TASK_WORK_SPACE));
        assertEquals("123", MDC.get(TASK_ID));
        assertEquals("456", MDC.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.trace("1", "123", "456");
        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());
        
        assertEquals("1", MDC.get(TASK_WORK_SPACE));
        assertEquals("123", MDC.get(TASK_ID));
        assertEquals("456", MDC.get("organizationId"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("1", "123", "456");
        OnlineSchemaChangeContextHolder.clear();
        
        assertNull(MDC.get(TASK_WORK_SPACE));
        assertNull(MDC.get(TASK_ID));
        assertNull(MDC.get("organizationId"));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace("1", "123", "456");
        
        assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
        assertEquals("123", OnlineSchemaChangeContextHolder.get(TASK_ID));
        assertEquals("456", OnlineSchemaChangeContextHolder.get("organizationId"));
    }
}
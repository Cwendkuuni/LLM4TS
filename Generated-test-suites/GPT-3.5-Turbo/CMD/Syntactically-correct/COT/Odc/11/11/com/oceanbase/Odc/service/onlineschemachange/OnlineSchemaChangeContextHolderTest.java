import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        // Set up any test data or mock objects needed for testing
    }

    @Test
    public void testTraceLong() {
        // Test trace method with Long parameters
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        // Test trace method with String parameters
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        // Test retrace method
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        // Test clear method
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        OnlineSchemaChangeContextHolder.clear();
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

}
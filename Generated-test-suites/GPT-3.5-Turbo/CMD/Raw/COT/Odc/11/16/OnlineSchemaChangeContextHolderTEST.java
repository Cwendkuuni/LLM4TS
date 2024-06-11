import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(MDC.getCopyOfContextMap());
        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    // Potential edge cases and exceptions to test: 
    // 1. Passing null values to retrace method
    // 2. Getting a key that does not exist in the context map

    @Test
    public void testRetraceNullValues() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        OnlineSchemaChangeContextHolder.retrace(null);
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGetNonExistingKey() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertNull(OnlineSchemaChangeContextHolder.get("nonExistingKey"));
    }

}
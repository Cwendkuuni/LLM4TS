import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTrace_Long() {
        // Typical use case
		OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);

		// Verify that the MDC has been correctly updated
		assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
		assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
		assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTrace_String() {
        // Edge cases
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        // Verify that the MDC remains unchanged
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
		assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
		assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        // Error scenario - empty context
		Map<String, String> emptyContext = new HashMap<>();
		OnlineSchemaChangeContextHolder.retrace(emptyContext);

		// Verify that MDC context is not set
		assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        // Typical use case
		OnlineSchemaChangeContextHolder.clear();

		// Verify that the MDC context is cleared
		assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
		assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
		assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        // Edge case - key not present
        assertNull(OnlineSchemaChangeContextHolder.get("NonExistentKey"));
    }

}
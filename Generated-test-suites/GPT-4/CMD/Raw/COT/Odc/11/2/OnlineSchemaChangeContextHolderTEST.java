import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    private MultiValueMap<String, String> context;

    @Before
    public void setUp() {
        // Initialize context map
        context = new LinkedMultiValueMap<>();
        context.add(OnlineSchemaChangeContextHolder.TASK_ID, "testTaskId");
        context.add(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "testWorkSpace");
        context.add(OdcConstants.ORGANIZATION_ID, "testOrgId");
    }

    @Test
    public void testTraceWithLongParameters() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithStringInvalidParameters() {
        OnlineSchemaChangeContextHolder.trace("invalid", "invalid", "invalid");
    }

    @Test
    public void testTraceWithStringValidParameters() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceWithNullParameters() {
        OnlineSchemaChangeContextHolder.trace((String) null, null, null);
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(this.context.toSingleValueMap());
        assertEquals("testTaskId", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("testWorkSpace", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("testOrgId", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "testGet");
        assertEquals("testGet", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @After
    public void tearDown() {
        // Clear MDC after each test
        MDC.clear();
    }
}
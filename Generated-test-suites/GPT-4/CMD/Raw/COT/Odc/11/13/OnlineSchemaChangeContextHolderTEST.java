
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {
    private long testUserId;
    private String testUserIdStr;
    private long testTaskId;
    private String testTaskIdStr;
    private long testOrganizationId;
    private String testOrganizationIdStr;
    private Map<String, String> testContextMap;

    @Before
    public void setUp() {
        testUserId = 1L;
        testUserIdStr = "1";
        testTaskId = 2L;
        testTaskIdStr = "2";
        testOrganizationId = 3L;
        testOrganizationIdStr = "3";

        testContextMap = new HashMap<>();
        testContextMap.put(OnlineSchemaChangeContextHolder.TASK_ID, testTaskIdStr);
        testContextMap.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, testUserIdStr);
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTrace() {
        OnlineSchemaChangeContextHolder.trace(testUserId, testTaskId, testOrganizationId);

        assertEquals(testUserIdStr, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(testTaskIdStr, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace(testUserIdStr, testTaskIdStr, testOrganizationIdStr);

        assertEquals(testUserIdStr, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(testTaskIdStr, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(testUserId, testTaskId, testOrganizationId);
        OnlineSchemaChangeContextHolder.clear();

        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void testRetrace() {
        OnlineSchemaChangeContextHolder.retrace(testContextMap);

        assertEquals(testUserIdStr, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(testTaskIdStr, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringThrowsNumberFormatException() {
        String invalidNumber = "not a number";
        OnlineSchemaChangeContextHolder.trace(invalidNumber, invalidNumber, invalidNumber);
    }

    @Test(expected = NullPointerException.class)
    public void testRetraceThrowsNullPointerException() {
        OnlineSchemaChangeContextHolder.retrace(null);
    }
}

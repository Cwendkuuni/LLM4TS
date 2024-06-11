import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

public class OnlineSchemaChangeContextHolderTest {

    private static final String TASK_ID = "1000";
    private static final String USER_ID = "200";
    private static final String ORGANIZATION_ID = "3000";
    
    // Prepare initial context
    private Map<String, String> context;
    
    @Before
    public void setUp() throws Exception {
        context = new HashMap<String, String>();
        context.put("taskId", TASK_ID);
        context.put("userId", USER_ID);
        context.put("organizationId", ORGANIZATION_ID);
    }

    @Test
    public void traceTest() {
        OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);
        
        String taskId = OnlineSchemaChangeContextHolder.get("taskId");
        assertEquals(TASK_ID, taskId);
    }
    
    @Test
    public void retraceTest() {
        OnlineSchemaChangeContextHolder.retrace(context);
        
        String taskId = OnlineSchemaChangeContextHolder.get("taskId");
        assertEquals(TASK_ID, taskId);
    }

    @Test
    public void clearTest() {
        OnlineSchemaChangeContextHolder.clear();
        
        String taskId = OnlineSchemaChangeContextHolder.get("taskId");
        assertNull(taskId);
    }
    
    @Test(expected = NumberFormatException.class)
    public void traceTest_NumberFormatException() {
        OnlineSchemaChangeContextHolder.trace("NotANumber", TASK_ID, ORGANIZATION_ID);
    }
    
    @Test(expected = NullPointerException.class)
    public void traceTest_NullPointerException() {
        OnlineSchemaChangeContextHolder.trace(null, TASK_ID, ORGANIZATION_ID);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void retraceTest_IllegalArgumentException() {
        Map<String, String> invalidContext = null;
        OnlineSchemaChangeContextHolder.retrace(invalidContext);
    }

    @After
    public void tearDown() throws Exception {
        // Clean up after each test
        OnlineSchemaChangeContextHolder.clear();
    }
}
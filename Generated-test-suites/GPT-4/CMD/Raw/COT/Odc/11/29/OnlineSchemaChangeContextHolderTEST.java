
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.junit.Test;
import java.util.HashMap;
import org.slf4j.MDC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        //... continue with all the fields
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        //... continue with all the fields
    }

    @Test
    public void testRetrace() {
        HashMap<String, String> context = new HashMap<>();
        context.put("key", "value");
        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("value", MDC.get("key"));
    }

    @Test
    public void testClear() {
        MDC.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
       //... continue with all the fields
    }

    @Test
    public void testGet() {
        MDC.put("key", "value");
        assertEquals("value", OnlineSchemaChangeContextHolder.get("key"));
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringWithNonNumeric() {
        OnlineSchemaChangeContextHolder.trace("a", "b", "c");
    }

    //we can continue with more edge cases
}

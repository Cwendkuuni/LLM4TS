import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        assertEquals("1", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", MDC.get("taskId"));
        assertEquals("3", MDC.get("organizationId"));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        assertEquals("1", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", MDC.get("taskId"));
        assertEquals("3", MDC.get("organizationId"));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("onlineSchemaChangeWorkSpace", "1");
        context.put("taskId", "2");
        context.put("organizationId", "3");

        OnlineSchemaChangeContextHolder.retrace(context);
        assertEquals("1", MDC.get("onlineSchemaChangeWorkSpace"));
        assertEquals("2", MDC.get("taskId"));
        assertEquals("3", MDC.get("organizationId"));
    }

    @Test
    public void testClear() {
        MDC.put("test", "value");
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get("test"));
    }

    @Test
    public void testGet() {
        MDC.put("test", "value");
        assertEquals("value", OnlineSchemaChangeContextHolder.get("test"));
    }
}


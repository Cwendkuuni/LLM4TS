
package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.slf4j.MDC;
import java.util.Map;
import java.util.HashMap;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setup() {
        MDC.clear();
    } 

    @After
    public void teardown() {
        MDC.clear();
    }

    @Test
    public void testTrace_ShouldFillMDC() {
        OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
        Assert.assertNotNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNotNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNotNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString_ShouldParseAndFillMDC() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");
        Assert.assertNotNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertNotNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertNotNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace_ShouldSetContextMap() {
        Map<String, String> context = new HashMap<>();
        context.put("key", "value");
        OnlineSchemaChangeContextHolder.retrace(context);
        Assert.assertEquals("value", MDC.get("key"));
    }

    @Test
    public void testClear_ShouldClearMDC() {
        MDC.put("key", "value");
        OnlineSchemaChangeContextHolder.clear();
        Assert.assertNull(MDC.get("key"));
    }

    @Test
    public void testGet_ShouldGetValueFromMDC() {
        MDC.put("key", "value");
        String value = OnlineSchemaChangeContextHolder.get("key");
        Assert.assertEquals("value", value);
    }

}

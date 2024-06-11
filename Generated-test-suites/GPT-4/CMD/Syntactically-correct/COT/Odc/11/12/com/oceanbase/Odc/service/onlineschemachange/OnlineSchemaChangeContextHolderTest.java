package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Test;
import org.junit.Assert;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void testTraceWithLongValues() {
        OnlineSchemaChangeContextHolder.trace(Long.valueOf(1), Long.valueOf(2), Long.valueOf(3));
        Assert.assertEquals(MDC.get("taskId"), "2");
        Assert.assertEquals(MDC.get("onlineSchemaChangeWorkSpace"), "1");
        Assert.assertEquals(MDC.get(OdcConstants.ORGANIZATION_ID), "3");
    }

    @Test
    public void testTraceWithStringValues() {
        OnlineSchemaChangeContextHolder.trace("4", "5", "6");
        Assert.assertEquals(MDC.get("taskId"), "5");
        Assert.assertEquals(MDC.get("onlineSchemaChangeWorkSpace"), "4");
        Assert.assertEquals(MDC.get(OdcConstants.ORGANIZATION_ID), "6");
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceWithStringValuesException() {
        OnlineSchemaChangeContextHolder.trace("a", "5", "6");
    }

    @Test
    public void testRetrace() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        OnlineSchemaChangeContextHolder.retrace(map);
        Assert.assertEquals(MDC.get("key1"), "value1");
    }

    @Test(expected = NullPointerException.class)
    public void testRetraceException() {
        OnlineSchemaChangeContextHolder.retrace(null);
    }

    @Test
    public void testClear() {
        MDC.put("A key", "A value");
        OnlineSchemaChangeContextHolder.clear();
        Assert.assertEquals(MDC.getCopyOfContextMap(), null);
    }

    @Test
    public void testGet() {
        MDC.put("SomeKey", "SomeValue");
        String value = OnlineSchemaChangeContextHolder.get("SomeKey");
        Assert.assertEquals(value, "SomeValue");
    }

    @Test
    public void testGetNonExistingKey() {
        String value = OnlineSchemaChangeContextHolder.get("NonExistingKey");
        Assert.assertEquals(value, null);
    }
}
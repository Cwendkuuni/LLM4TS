package com.oceanbase.odc.service.onlineschemachange;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.MDC;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

public class OnlineSchemaChangeContextHolderTest {

    private static final String TEST_VALUE = "testValue";

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void traceLongTest() {
        // Test with valid values
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        Assert.assertEquals("123", MDC.get("onlineSchemaChangeWorkSpace"));
        Assert.assertEquals("456", MDC.get("taskId"));
        Assert.assertEquals("789", MDC.get("organizationId"));
	
        // Test with null values
	OnlineSchemaChangeContextHolder.trace(null, null, null);
	Assert.assertNull(MDC.get("onlineSchemaChangeWorkSpace"));
	Assert.assertNull(MDC.get("taskId"));
	Assert.assertNull(MDC.get("organizationId"));
    }

    @Test
    public void traceStringTest() {
        // Test with valid values
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");
        Assert.assertEquals("123", MDC.get("onlineSchemaChangeWorkSpace"));
        Assert.assertEquals("456", MDC.get("taskId"));
        Assert.assertEquals("789", MDC.get("organizationId"));
	
        // Test with null values
	OnlineSchemaChangeContextHolder.trace(null, null, null);
	Assert.assertNull(MDC.get("onlineSchemaChangeWorkSpace"));
	Assert.assertNull(MDC.get("taskId"));
	Assert.assertNull(MDC.get("organizationId"));
    }

    @Test
    public void retraceTest() {
        Map<String, String> contextMap = new HashMap<>();
        // Test with valid context map
        contextMap.put("onlineSchemaChangeWorkSpace", "123");
        contextMap.put("taskId", "456");
        contextMap.put("organizationId", "789");
        OnlineSchemaChangeContextHolder.retrace(contextMap);
        Assert.assertEquals("123", MDC.get("onlineSchemaChangeWorkSpace"));
        Assert.assertEquals("456", MDC.get("taskId"));
        Assert.assertEquals("789", MDC.get("organizationId"));
	
	// Test with empty context map
	contextMap.clear();
	OnlineSchemaChangeContextHolder.retrace(contextMap);
	Assert.assertNull(MDC.get("onlineSchemaChangeWorkSpace"));
	Assert.assertNull(MDC.get("taskId"));
	Assert.assertNull(MDC.get("organizationId"));
    }

    @Test
    public void clearTest() {
        // Add some variables to MDC
        MDC.put("onlineSchemaChangeWorkSpace", "123");
        MDC.put("taskId", "456");
        MDC.put("organizationId", "789");
        // Verify clearing
        OnlineSchemaChangeContextHolder.clear();
        Assert.assertNull(MDC.get("onlineSchemaChangeWorkSpace"));
        Assert.assertNull(MDC.get("taskId"));
        Assert.assertNull(MDC.get("organizationId"));
    }

    @Test
    public void getTest() {
        // Test get with existing key
        MDC.put("onlineSchemaChangeWorkSpace", TEST_VALUE);
        Assert.assertEquals(TEST_VALUE, OnlineSchemaChangeContextHolder.get("onlineSchemaChangeWorkSpace"));
	
        // Test get with non-existing key
	Assert.assertNull(OnlineSchemaChangeContextHolder.get("nonExistingKey"));
    }
}
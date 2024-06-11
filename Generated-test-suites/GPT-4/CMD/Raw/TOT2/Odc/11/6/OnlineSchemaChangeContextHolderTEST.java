
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void clearContextBeforeTest() {
        MDC.clear();
    }

    @Test
    public void traceShouldSetCorrectValuesWithLongs() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);

        Assert.assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
        // Add validation for SecurityContextUtils.getCurrentUser()
    }

    @Test
    public void traceShouldSetCorrectValuesWithStrings() {
        OnlineSchemaChangeContextHolder.trace("123", "456", "789");

        Assert.assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        Assert.assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        Assert.assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
        // Add validation for SecurityContextUtils.getCurrentUser()
    }

    @Test(expected = NumberFormatException.class)
    public void traceShouldThrowExceptionWhenPassedNonNumericStrings() {
        OnlineSchemaChangeContextHolder.trace("abc", "def", "ghi");
    }

    @Test
    public void retraceShouldSetContextMap() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");

        OnlineSchemaChangeContextHolder.retrace(context);

        Assert.assertEquals("value1", MDC.get("key1"));
    }

    @Test
    public void clearShouldEmptyContext() {
        MDC.put("key1", "value1");

        OnlineSchemaChangeContextHolder.clear();

        Assert.assertNull(MDC.get("key1"));
    }

    @Test
    public void getShouldReturnCorrectValue() {
        MDC.put("key1", "value1");

        Assert.assertEquals("value1", OnlineSchemaChangeContextHolder.get("key1"));
    }

    @Test
    public void getShouldReturnNullWhenKeyNotPresent() {
        Assert.assertNull(OnlineSchemaChangeContextHolder.get("nonexistentKey"));
    }
}

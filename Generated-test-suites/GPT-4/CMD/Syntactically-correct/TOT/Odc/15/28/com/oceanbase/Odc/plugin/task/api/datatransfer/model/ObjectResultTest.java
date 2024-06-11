package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Assert;
import org.junit.Test;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        Assert.assertEquals("testName", objectResult.getName());
        Assert.assertEquals("testType", objectResult.getType());
        Assert.assertEquals("testSchema", objectResult.getSchema());
    }

    @Test
    public void testOfMethod() {
        AtomicLong count = new AtomicLong(50);
        AtomicLong total = new AtomicLong(100);
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setCount(count);
        objectStatus.setTotal(total);

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assert.assertEquals(50, objectResult.getCount().get());
        Assert.assertEquals(100, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        Assert.assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void testOfMethodWithNull() {
        ObjectResult.of(null);
    }

    @Test
    public void testGetSummaryWithNulls() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
        Assert.assertEquals("null.null[null]", objectResult.getSummary());
    }
}

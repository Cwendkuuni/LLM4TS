package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    private ObjectStatus objectStatus;
    private ObjectResult objectResult;

    @Before
    public void init() {
        AtomicLong count = new AtomicLong(10);
        AtomicLong total = new AtomicLong(20);
        objectStatus = new ObjectStatus("testName", count, total, "testStatus");
        objectResult = new ObjectResult("testSchema","testName", "testType");
    }

    @Test
    public void testObjectResult() {
        Assert.assertEquals("testSchema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultOf() {
        ObjectResult result = ObjectResult.of(objectStatus);
        // Assuming that ObjectStatus.getCount() would return an AtomicLong object
        Assert.assertEquals(objectStatus.getCount().get(), result.getCount().get());
        Assert.assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
        Assert.assertEquals(objectStatus.getType(), result.getType());
        Assert.assertEquals(objectStatus.getName(), result.getName());
        Assert.assertEquals(objectStatus.getSchema(), result.getSchema());
        Assert.assertEquals(objectStatus.getStatus(), result.getStatus());
    }
}
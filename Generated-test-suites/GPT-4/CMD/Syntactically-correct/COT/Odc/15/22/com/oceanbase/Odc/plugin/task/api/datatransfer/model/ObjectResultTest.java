package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicLong;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        Assert.assertEquals("schema1", objectResult.getSchema());
        Assert.assertEquals("name1", objectResult.getName());
        Assert.assertEquals("type1", objectResult.getType());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus(
            "name1", new AtomicLong(1), new AtomicLong(2), "Done");
        objectStatus.setSchema("schema1");
        objectStatus.setType("type1");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assert.assertEquals(objectStatus.getName(), objectResult.getName());
        Assert.assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        Assert.assertEquals(objectStatus.getType(), objectResult.getType());
        Assert.assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        Assert.assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        Assert.assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        Assert.assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void testOfMethodWithNull() {
        ObjectResult.of(null);
    }

    @Test
    public void testConstructorWithNull() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
        Assert.assertNull(objectResult.getSchema());
        Assert.assertNull(objectResult.getName());
        Assert.assertNull(objectResult.getType());
    }

    @Test
    public void testGetSummaryWithNulls() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
        Assert.assertEquals("null.null[null]", objectResult.getSummary());
    }

}
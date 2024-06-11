package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import org.junit.Test;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Assert;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResult() {

        ObjectResult objectResult = new ObjectResult("aSchema", "aName", "aType");
        Assert.assertEquals("aSchema", objectResult.getSchema());
        Assert.assertEquals("aName", objectResult.getName());
        Assert.assertEquals("aType", objectResult.getType());
        Assert.assertEquals(new AtomicLong(0).get(), objectResult.getCount().get());
        Assert.assertEquals(new AtomicLong(0).get(), objectResult.getTotal().get());
        // Test also for null and empty String inputs, and other edge cases.

    }
 
    @Test
    public void testObjectResultOf() {
        ObjectStatus that = new ObjectStatus("thatName", new AtomicLong(0), new AtomicLong(0), null);
        that.setType("thatType");
        that.setSchema("thatSchema");

        ObjectResult objectResult = ObjectResult.of(that);
        Assert.assertEquals(that.getType(), objectResult.getType());
        Assert.assertEquals(that.getStatus(), objectResult.getStatus());
        Assert.assertEquals(that.getName(), objectResult.getName());
        Assert.assertEquals(that.getSchema(), objectResult.getSchema());
        Assert.assertEquals(that.getCount().get(), objectResult.getCount().get());
        Assert.assertEquals(that.getTotal().get(), objectResult.getTotal().get());
        // Again, don't forget to test method for possible null inputs and other edge cases.
    }
  
    @Test
    public void testGetSummary() {

        ObjectResult objectResult = new ObjectResult("aSchema", "aName", "aType");
        Assert.assertEquals("aSchema.aName[aType]", objectResult.getSummary());

    }

    // Add more @Test methods to check for various typical, edge and error cases.
    // A good practice is also to test all class methods for null inputs to ensure that the class handles such cases correctly.
  
}
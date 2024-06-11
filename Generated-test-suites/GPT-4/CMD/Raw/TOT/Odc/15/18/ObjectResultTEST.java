
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    private ObjectResult objectResult;

    @Before
    public void setup() {
        objectResult = new ObjectResult("schema", "name", "type");
    }

    @Test
    // Test "of" method
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), "status");
        objectStatus.setType("type");
        objectStatus.setSchema("schema");
        ObjectResult result = ObjectResult.of(objectStatus);
        Assert.assertEquals(objectResult.getType(), result.getType());
        Assert.assertEquals(objectResult.getSchema(), result.getSchema());
        Assert.assertEquals(objectResult.getName(), result.getName());
        Assert.assertEquals(objectResult.getStatus(), result.getStatus());
        Assert.assertEquals(objectResult.getCount().get(), result.getCount().get());
        Assert.assertEquals(objectResult.getTotal().get(), result.getTotal().get());
    }

    @Test
    // Test "getSummary" method
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        Assert.assertEquals("schema.name[type]", summary);
    }

   @Test
    // Test for null object
    public void testOfForNullObject() {
        ObjectStatus objectStatus = null;
        ObjectResult result = ObjectResult.of(objectStatus);
        Assert.assertNull(result);
    }

    @Test
    // Test for null values in input object
    public void testOfForNullValues() {
        ObjectStatus objectStatus = new ObjectStatus(null, null, null, null);
        ObjectResult result = ObjectResult.of(objectStatus);
        Assert.assertNull(result.getName());
        Assert.assertNull(result.getSchema());
        Assert.assertNull(result.getType());
        Assert.assertNull(result.getStatus());
        Assert.assertNull(result.getCount());
        Assert.assertNull(result.getTotal());
    }

 }

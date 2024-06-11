import org.junit.Assert;
import org.junit.Test;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testDefaultConstructor() {
        ObjectResult objectResult = new ObjectResult();
        Assert.assertNull(objectResult.getSchema());
    }

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        Assert.assertEquals("schema", objectResult.getSchema());
        Assert.assertEquals("name", objectResult.getName());
        Assert.assertEquals("type", objectResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        Assert.assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testGetSummaryNullValues() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
        Assert.assertEquals(".[null]", objectResult.getSummary());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assert.assertNotNull(objectResult);
    }

    @Test(expected = NullPointerException.class)
    public void testOfNullObjectStatus() {
        ObjectResult objectResult = ObjectResult.of(null);
    }
}
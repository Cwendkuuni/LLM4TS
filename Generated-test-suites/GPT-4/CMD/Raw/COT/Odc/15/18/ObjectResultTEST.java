import com.oceanbase.odc.plugin.task.api.datatransfer.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test(expected = NullPointerException.class)
    public void testConstructorNullArguments() {
        new ObjectResult(null, null, null);
    }

    @Test
    public void testConstructorNonNullArguments() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        Assert.assertEquals("test_schema", objectResult.getSchema());
        Assert.assertEquals("test_name", objectResult.getName());
        Assert.assertEquals("test_type", objectResult.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testOfMethodNullArgument() {
        ObjectResult.of(null);
    }

    @Test
    public void testOfMethodNonNullArgument() {
        ObjectStatus objectStatus = new ObjectStatus("test_name", new AtomicLong(1), new AtomicLong(1), null);
        objectStatus.setType("test_type");
        objectStatus.setSchema("test_schema");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assert.assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        Assert.assertEquals(objectStatus.getName(), objectResult.getName());
        Assert.assertEquals(objectStatus.getType(), objectResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        String expectedSummary = "test_schema.test_name[test_type]";
        String actualSummary = objectResult.getSummary();
        Assert.assertEquals(expectedSummary, actualSummary);
    }
}
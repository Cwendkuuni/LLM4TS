import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    // Test constructor
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        Assert.assertEquals("name", objectResult.getName());
        Assert.assertEquals("type", objectResult.getType());
        Assert.assertEquals("schema", objectResult.getSchema());
        Assert.assertNull(objectResult.getExportPaths());
        Assert.assertEquals(0, objectResult.getCount().get());
        Assert.assertEquals(0, objectResult.getTotal().get());
    }

    // Test of method: of
    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", "type", new AtomicLong(1), new AtomicLong(2), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assert.assertEquals(objectStatus.getName(), objectResult.getName());
        Assert.assertEquals(objectStatus.getType(), objectResult.getType());
        Assert.assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        Assert.assertNotNull(objectResult.getCount());
        Assert.assertNotNull(objectResult.getTotal());
        Assert.assertNull(objectResult.getExportPaths());
    }

    // Test getSummary method
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String expectedSummary = "schema.name[type]";
        String actualSummary = objectResult.getSummary();
        Assert.assertEquals(expectedSummary, actualSummary);
    }

    // Edge case tests

    // Test empty constructor with null values
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructorNullValues() {
        new ObjectResult(null, null, null);
    }

    // Test of method: getSummary with null schema or name
    @Test
    public void testGetSummaryNullValues() {
        ObjectResult objectResult = new ObjectResult(null, "name", "type");
        Assert.assertNull(objectResult.getSummary());

        objectResult = new ObjectResult("schema", null, "type");
        Assert.assertNull(objectResult.getSummary());
    }
}
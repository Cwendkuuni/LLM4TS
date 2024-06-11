import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    // Test constructor
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        Assert.assertEquals("schema", objectResult.getSchema());
        Assert.assertEquals("name", objectResult.getName());
        Assert.assertEquals("type", objectResult.getType());
    }

    // Test of method: of
    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assert.assertEquals("name", objectResult.getName());
        Assert.assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        Assert.assertNull(objectResult.getExportPaths());
    }

    // Test getSummary method
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema");
        objectResult.setName("name");
        objectResult.setType("type");
        String expected = "schema.name[type]";
        Assert.assertEquals(expected, objectResult.getSummary());
    }

    // Edge case testing for constructor
    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorNullSchema() {
        new ObjectResult(null, "name", "type");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorNullName() {
        new ObjectResult("schema", null, "type");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorNullType() {
        new ObjectResult("schema", "name", null);
    }

    // Edge case testing for of method
    @Test(expected = NullPointerException.class)
    public void testObjectResultOfNullObjectStatus() {
        ObjectResult.of(null);
    }
}
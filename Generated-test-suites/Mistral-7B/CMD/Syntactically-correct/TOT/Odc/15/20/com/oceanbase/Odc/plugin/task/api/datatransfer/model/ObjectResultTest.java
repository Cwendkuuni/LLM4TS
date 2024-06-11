import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";
        ObjectResult objectResult = new ObjectResult(schema, name, type);
        Assert.assertEquals(schema, objectResult.getSchema());
        Assert.assertEquals(name, objectResult.getName());
        Assert.assertEquals(type, objectResult.getType());
        Assert.assertNull(objectResult.getExportPaths());
        Assert.assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        Assert.assertEquals(0, objectResult.getCount().get());
        Assert.assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema("testSchema");
        objectStatus.setName("testName");
        objectStatus.setType("testType");
        objectStatus.setStatus(ObjectStatus.Status.SUCCESS);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assert.assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        Assert.assertEquals(objectStatus.getName(), objectResult.getName());
        Assert.assertEquals(objectStatus.getType(), objectResult.getType());
        Assert.assertNull(objectResult.getExportPaths());
        Assert.assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        Assert.assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        Assert.assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("testSchema");
        objectResult.setName("testName");
        objectResult.setType("testType");
        String expectedSummary = "testSchema.testName[testType]";
        Assert.assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testNegativeCases() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema(null);
        objectResult.setName(null);
        objectResult.setType(null);
        Assert.assertNull(objectResult.getSchema());
        Assert.assertNull(objectResult.getName());
        Assert.assertNull(objectResult.getType());
        Assert.assertNull(objectResult.getExportPaths());
        Assert.assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        Assert.assertEquals(0, objectResult.getCount().get());
        Assert.assertEquals(0, objectResult.getTotal().get());

        objectResult.setStatus(null);
        Assert.assertNull(objectResult.getStatus());
        Assert.assertEquals(0, objectResult.getCount().get());
        Assert.assertEquals(0, objectResult.getTotal().get());
    }

    @Test(expected = NullPointerException.class)
    public void testSetExportPathsNull() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(null);
    }
}
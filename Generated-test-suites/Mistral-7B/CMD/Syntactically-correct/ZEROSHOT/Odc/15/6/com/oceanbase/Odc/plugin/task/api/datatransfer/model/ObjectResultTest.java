import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.AtomicLong;
import java.util.List;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schema", "name", "type");
    }

    @Test
    public void testObjectResultConstructor() {
        Assert.assertEquals("schema", objectResult.getSchema());
        Assert.assertEquals("name", objectResult.getName());
        Assert.assertEquals("type", objectResult.getType());
        Assert.assertNotNull(objectResult.getStatus());
        Assert.assertNotNull(objectResult.getCount());
        Assert.assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = "schema.name[type]";
        Assert.assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("type");
        ObjectResult result = ObjectResult.of(objectStatus);
        Assert.assertEquals(objectStatus.getType(), result.getType());
        Assert.assertEquals(objectStatus.getStatus(), result.getStatus());
        Assert.assertEquals(objectStatus.getName(), result.getName());
        Assert.assertEquals(objectStatus.getSchema(), result.getSchema());
        Assert.assertEquals(objectStatus.getCount().get(), result.getCount().get());
        Assert.assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testSetType() {
        objectResult.setType("newType");
        Assert.assertEquals("newType", objectResult.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testSetTypeWithNull() {
        objectResult.setType(null);
    }

    @Test
    public void testSetStatus() {
        objectResult.setStatus(ObjectStatus.Status.SUCCEEDED);
        Assert.assertEquals(ObjectStatus.Status.SUCCEEDED, objectResult.getStatus());
    }

    @Test(expected = NullPointerException.class)
    public void testSetStatusWithNull() {
        objectResult.setStatus(null);
    }

    @Test
    public void testSetName() {
        objectResult.setName("newName");
        Assert.assertEquals("newName", objectResult.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameWithNull() {
        objectResult.setName(null);
    }

    @Test
    public void testSetSchema() {
        objectResult.setSchema("newSchema");
        Assert.assertEquals("newSchema", objectResult.getSchema());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSchemaWithNull() {
        objectResult.setSchema(null);
    }

    @Test
    public void testGetCount() {
        AtomicLong count = new AtomicLong(1);
        objectResult.setCount(count);
        Assert.assertEquals(count, objectResult.getCount());
    }

    @Test
    public void testGetTotal() {
        AtomicLong total = new AtomicLong(2);
        objectResult.setTotal(total);
        Assert.assertEquals(total, objectResult.getTotal());
    }

    @Test
    public void testGetExportPathsIsEmptyByDefault() {
        Assert.assertTrue(objectResult.getExportPaths() == null || objectResult.getExportPaths().isEmpty());
    }

    @Test
    public void testSetExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("file://test.csv"));
        objectResult.setExportPaths(exportPaths);
        Assert.assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
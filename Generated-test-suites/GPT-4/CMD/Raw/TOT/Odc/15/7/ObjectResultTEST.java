import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        Assert.assertEquals("schema", objectResult.getSchema());
        Assert.assertEquals("name", objectResult.getName());
        Assert.assertEquals("type", objectResult.getType());
    }
    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), "status");
        objectStatus.setSchema("schema");
        objectStatus.setType("type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
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
    public void testExportPathsSetAndGet() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        ArrayList<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost:8080/"));
        objectResult.setExportPaths(exportPaths);
        Assert.assertEquals(exportPaths, objectResult.getExportPaths());
    }
    @Test(expected = NullPointerException.class)
    public void testNullObjectStatus() {
        ObjectResult.of(null);
    }
}
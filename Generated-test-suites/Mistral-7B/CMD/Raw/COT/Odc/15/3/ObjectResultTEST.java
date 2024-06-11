import org.junit.Test;
import org.junit.Assert.*;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        Assert.assertEquals("name", objectResult.getName());
        Assert.assertEquals("type", objectResult.getType());
        Assert.assertEquals("schema", objectResult.getSchema());
        Assert.assertEquals(0, objectResult.getCount().get());
        Assert.assertEquals(0, objectResult.getTotal().get());
        Assert.assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(1), new AtomicLong(2), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assert.assertEquals("name", objectResult.getName());
        Assert.assertEquals(1, objectResult.getCount().get());
        Assert.assertEquals(2, objectResult.getTotal().get());
        Assert.assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String summary = objectResult.getSummary();
        Assert.assertEquals("schema.name[type]", summary);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryNullSchema() {
        ObjectResult objectResult = new ObjectResult(null, "name", "type");
        String summary = objectResult.getSummary();
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryNullName() {
        ObjectResult objectResult = new ObjectResult("schema", null, "type");
        String summary = objectResult.getSummary();
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryNullType() {
        ObjectResult objectResult = new ObjectResult("schema", "name", null);
        String summary = objectResult.getSummary();
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        List<URL> exportPaths = new ArrayList<>();
        URL url = new URL("http://example.com");
        exportPaths.add(url);
        objectResult.setExportPaths(exportPaths);
        Assert.assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testEmptyExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        objectResult.setExportPaths(null);
        Assert.assertNull(objectResult.getExportPaths());
    }

}
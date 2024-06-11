
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Assert;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");

        Assert.assertEquals("schema1", objectResult.getSchema());
        Assert.assertEquals("name1", objectResult.getName());
        Assert.assertEquals("type1", objectResult.getType());
        Assert.assertEquals(new AtomicLong(0), objectResult.getCount());
        Assert.assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(0), new AtomicLong(0), null);
        objectStatus.setType("type1");
        objectStatus.setSchema("schema1");

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        Assert.assertEquals("schema1", objectResult.getSchema());
        Assert.assertEquals("name1", objectResult.getName());
        Assert.assertEquals("type1", objectResult.getType());
        Assert.assertEquals(new AtomicLong(0), objectResult.getCount());
        Assert.assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");

        Assert.assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://www.example.com"));
        
        objectResult.setExportPaths(exportPaths);

        Assert.assertEquals(exportPaths, objectResult.getExportPaths());
    }

    // Test exception handling.
    @Test(expected = NullPointerException.class)
    public void testObjectResultOfWithNullException() {
        ObjectResult.of(null);
    }
}

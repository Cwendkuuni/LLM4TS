import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.net.URL;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertNotNull(objectResult);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(100), new AtomicLong(200), "type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals("name", objectResult.getName());
        assertEquals(100, objectResult.getCount().get());
        assertEquals(200, objectResult.getTotal().get());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String summary = objectResult.getSummary();
        assertEquals("schema.name[type]", summary);
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = List.of(new URL("http://example.com"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
import static org.junit.Assert.*;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(5), new AtomicLong(10), "type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("name", objectResult.getName());
        assertEquals(5, objectResult.getCount().get());
        assertEquals(10, objectResult.getTotal().get());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        try {
            exportPaths.add(new URL("http://www.example.com"));
        } catch (MalformedURLException e) {
            fail("MalformedURLException should not be thrown");
        }

        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        objectResult.setExportPaths(exportPaths);

        assertEquals(1, objectResult.getExportPaths().size());
        assertEquals("http://www.example.com", objectResult.getExportPaths().get(0).toString());
    }

}
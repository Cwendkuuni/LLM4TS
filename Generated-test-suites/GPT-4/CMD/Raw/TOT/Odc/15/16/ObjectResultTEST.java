import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicLong;
import java.net.URL;
import java.util.ArrayList;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult obj = new ObjectResult("schema_1", "name_1", "type_1");
        assertEquals("schema_1", obj.getSchema());
        assertEquals("name_1", obj.getName());
        assertEquals("type_1", obj.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus status = new ObjectStatus("name_1", new AtomicLong(0), new AtomicLong(0), null);
        status.setSchema("schema_1");
        status.setType("type_1");
        ObjectResult result = ObjectResult.of(status);
        assertEquals("schema_1", result.getSchema());
        assertEquals("name_1", result.getName());
        assertEquals("type_1", result.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult obj = new ObjectResult("schema_a", "name_a", "type_a");
        String expectedSummary = "schema_a.name_a[type_a]";
        assertEquals(expectedSummary, obj.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult obj = new ObjectResult();
        assertNull(obj.getExportPaths());
        List<URL> paths = new ArrayList<>();
        obj.setExportPaths(paths);
        assertEquals(paths, obj.getExportPaths());
    }
}
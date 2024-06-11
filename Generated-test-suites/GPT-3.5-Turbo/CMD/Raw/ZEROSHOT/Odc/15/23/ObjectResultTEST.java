import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertNotNull(result.getCount());
        assertNotNull(result.getTotal());
        assertNull(result.getExportPaths());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), "type");
        ObjectResult result = ObjectResult.of(status);
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        String summary = result.getSummary();
        assertEquals("schema.name[type]", summary);
    }

}
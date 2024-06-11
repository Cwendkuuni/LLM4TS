import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult obj = new ObjectResult("schema", "name", "type");
        assertEquals("schema", obj.getSchema());
        assertEquals("name", obj.getName());
        assertEquals("type", obj.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(1), new AtomicLong(10), null);
        ObjectResult obj = ObjectResult.of(status);
        assertEquals("name", obj.getName());
        assertEquals("UNKNOWN", obj.getType());
        assertEquals(1, obj.getCount().get());
        assertEquals(10, obj.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult obj = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", obj.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult obj = new ObjectResult();
        assertNull(obj.getExportPaths());
    }
}
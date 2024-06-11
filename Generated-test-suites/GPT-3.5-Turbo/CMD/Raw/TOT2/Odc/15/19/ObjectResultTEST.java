import org.junit.Test;
import static org.junit.Assert.*;

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
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        ObjectResult obj = ObjectResult.of(status);
        assertEquals("name", obj.getName());
        assertNull(obj.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult obj = new ObjectResult("schema", "name", "type");
        String summary = obj.getSummary();
        assertEquals("schema.name[type]", summary);
    }

}
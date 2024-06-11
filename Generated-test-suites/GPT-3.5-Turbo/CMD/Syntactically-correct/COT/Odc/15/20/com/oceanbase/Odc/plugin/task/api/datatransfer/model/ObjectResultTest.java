import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("testName", new AtomicLong(1), new AtomicLong(2), "testType");
        ObjectResult result = ObjectResult.of(status);
        assertEquals("testName", result.getName());
        assertEquals("testType", result.getType());
        assertEquals(1, result.getCount().get());
        assertEquals(2, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", result.getSummary());
    }

}
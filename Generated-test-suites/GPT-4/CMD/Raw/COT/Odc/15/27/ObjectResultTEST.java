import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstructor() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertNotNull(result);
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
    }

    @Test
    public void testObjectResultConstructorWithNull() {
        ObjectResult result = new ObjectResult(null, null, null);
        assertNull(result.getSchema());
        assertNull(result.getName());
        assertNull(result.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), "ready");
        status.setSchema("schema1");
        status.setType("type1");
        ObjectResult result = ObjectResult.of(status);
        assertEquals("schema1", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test(expected = NullPointerException.class)
    public void testOfWithNull() {
        ObjectResult.of(null);
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", result.getSummary());
    }
}
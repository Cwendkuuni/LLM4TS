import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0L, objectResult.getCount().get());
        assertEquals(0L, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("test", new AtomicLong(1), new AtomicLong(2), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("test", objectResult.getType());
        assertEquals(1L, objectResult.getCount().get());
        assertEquals(2L, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

}
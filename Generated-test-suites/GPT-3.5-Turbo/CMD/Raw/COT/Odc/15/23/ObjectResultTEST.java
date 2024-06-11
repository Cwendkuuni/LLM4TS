
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testStaticOfMethod() {
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(5), new AtomicLong(10), null);
        ObjectResult result = ObjectResult.of(status);

        assertEquals("name", result.getName());
        assertEquals(5, result.getCount().get());
        assertEquals(10, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");

        assertEquals("schema.name[type]", objectResult.getSummary());
    }

}


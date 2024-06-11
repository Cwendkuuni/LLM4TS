import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(1), new AtomicLong(2), "type");
        
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(2, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        
        String summary = objectResult.getSummary();
        
        assertEquals("schema.name[type]", summary);
    }
}
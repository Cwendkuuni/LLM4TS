import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(1), new AtomicLong(1), "type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        
        assertEquals("name", objectResult.getName());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(1, objectResult.getTotal().get());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String summary = objectResult.getSummary();
        
        assertEquals("schema.name[type]", summary);
    }

}
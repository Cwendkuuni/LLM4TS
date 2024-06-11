
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
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        
        assertEquals("name", objectResult.getName());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        
        String expectedSummary = "schema.name[type]";
        String actualSummary = objectResult.getSummary();
        
        assertEquals(expectedSummary, actualSummary);
    }
}

import static org.junit.Assert.*;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        AtomicLong count = new AtomicLong(5);
        AtomicLong total = new AtomicLong(10);
        ObjectStatus status = new ObjectStatus("name2", count, total, "status2");
        status.setType("type2");
        status.setSchema("schema2");
        
        ObjectResult objectResult = ObjectResult.of(status);
        assertEquals("schema2", objectResult.getSchema());
        assertEquals("name2", objectResult.getName());
        assertEquals("type2", objectResult.getType());
        assertEquals("status2", objectResult.getStatus());
        assertEquals(5, objectResult.getCount().get());
        assertEquals(10, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema3", "name3", "type3");
        String expectedSummary = "schema3.name3[type3]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }
}
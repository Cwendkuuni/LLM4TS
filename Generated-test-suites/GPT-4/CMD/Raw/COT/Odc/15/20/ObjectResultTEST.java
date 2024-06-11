import org.junit.Test;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult or = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", or.getSchema());
        assertEquals("name1", or.getName());
        assertEquals("type1", or.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus os = new ObjectStatus("name1", new AtomicLong(10), new AtomicLong(100), "successful");
        os.setType("type1");
        os.setSchema("schema1");
        ObjectResult or = ObjectResult.of(os);
        assertEquals("schema1", or.getSchema());
        assertEquals("name1", or.getName());
        assertEquals("type1", or.getType());
        assertEquals(10L, or.getCount().get());
        assertEquals(100L, or.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult or = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", or.getSummary());
    }
    
    @Test
    public void testObjectResultConstructorWithNullParameters() {
        ObjectResult or = new ObjectResult(null, null, null);
        assertNull(or.getSchema());
        assertNull(or.getName());
        assertNull(or.getType());
    }
    
    @Test(expected = NullPointerException.class)
    public void testOfWithNullObjectStatus() {
        ObjectResult.of(null);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name2", new AtomicLong(5), new AtomicLong(10), "type2");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("name2", objectResult.getName());
        assertEquals(5, objectResult.getCount().get());
        assertEquals(10, objectResult.getTotal().get());
        assertEquals("type2", objectResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema3", "name3", "type3");
        String summary = objectResult.getSummary();
        assertEquals("schema3.name3[type3]", summary);
    }

}
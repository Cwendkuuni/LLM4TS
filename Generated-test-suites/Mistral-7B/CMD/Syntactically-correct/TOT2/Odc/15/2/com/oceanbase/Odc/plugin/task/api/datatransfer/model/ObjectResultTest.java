import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals("schema", objectResult.getSchema());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("name", objectResult.getName());
        assertEquals("name", objectStatus.getName());
    }

    // Add test cases for other methods here
}
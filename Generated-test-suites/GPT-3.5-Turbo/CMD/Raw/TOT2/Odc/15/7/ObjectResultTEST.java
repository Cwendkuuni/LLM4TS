import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        assertEquals("test_schema", objectResult.getSchema());
        assertEquals("test_name", objectResult.getName());
        assertEquals("test_type", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("test_name", new AtomicLong(0), new AtomicLong(0), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("test_name", objectResult.getName());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        assertEquals("test_schema.test_name[test_type]", objectResult.getSummary());
    }

}
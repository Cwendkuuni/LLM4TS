import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(5), new AtomicLong(10), "testType");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("testName", objectResult.getName());
        assertEquals(5, objectResult.getCount().get());
        assertEquals(10, objectResult.getTotal().get());
        assertEquals("testType", objectResult.getType());
    }

    @Test
    public void testObjectResultSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

}
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(1), new AtomicLong(2), "testType");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(2, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummaryMethod() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        String summary = objectResult.getSummary();
        assertEquals("testSchema.testName[testType]", summary);
    }
}
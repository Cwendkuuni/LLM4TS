import org.junit.Test;
import static org.junit.Assert.*;

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
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(10), new AtomicLong(20), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("testName", objectResult.getName());
        assertEquals("DEFAULT", objectResult.getSchema());
        assertEquals("TYPE_UNKNOWN", objectResult.getType());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("testSchema");
        objectResult.setName("testName");
        objectResult.setType("testType");

        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

    // Additional test cases for edge cases and exception handling can be added here

}
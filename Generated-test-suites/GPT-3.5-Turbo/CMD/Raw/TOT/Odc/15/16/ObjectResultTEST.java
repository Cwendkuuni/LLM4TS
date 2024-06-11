import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertNotNull(objectResult);
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(10), new AtomicLong(20), "testType");
        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertNotNull(objectResult);
        assertEquals("testName", objectResult.getName());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
        assertEquals("testType", objectResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("testSchema");
        objectResult.setName("testName");
        objectResult.setType("testType");

        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

}
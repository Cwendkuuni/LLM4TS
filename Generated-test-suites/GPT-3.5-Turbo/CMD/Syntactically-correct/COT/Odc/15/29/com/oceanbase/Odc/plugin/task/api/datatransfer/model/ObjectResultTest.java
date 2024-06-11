import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        
        assertNotNull(objectResult);
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(10), new AtomicLong(20), "testType");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        
        assertNotNull(objectResult);
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        String summary = objectResult.getSummary();
        
        assertEquals("testSchema.testName[testType]", summary);
    }
}
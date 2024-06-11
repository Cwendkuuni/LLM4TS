import static org.junit.Assert.assertEquals;

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
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(100), new AtomicLong(200), "testType");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("testName", objectResult.getName());
        assertEquals(100, objectResult.getCount().get());
        assertEquals(200, objectResult.getTotal().get());
        assertEquals("testType", objectResult.getType());
    }
    
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        String expectedSummary = "testSchema.testName[testType]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }
}
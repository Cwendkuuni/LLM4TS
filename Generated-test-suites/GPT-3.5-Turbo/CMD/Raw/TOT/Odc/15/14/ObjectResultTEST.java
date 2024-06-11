
import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
      
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
    }
  
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        String expectedSummary = "testSchema.testName[testType]";
      
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(10), new AtomicLong(100), "testType");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
      
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(100, objectResult.getTotal().get());
    }

}

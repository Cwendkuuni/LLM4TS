
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(100), new AtomicLong(200), "testType");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(100, objectResult.getCount().get());
        assertEquals(200, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        String expectedSummary = "testSchema.testName[testType]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }
}

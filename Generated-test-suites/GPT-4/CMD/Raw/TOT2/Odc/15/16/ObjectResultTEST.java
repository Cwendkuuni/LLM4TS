import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicLong;
import java.net.URL;
import java.util.ArrayList;
import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;

public class ObjectResultTest {
    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("testSchema", "testName", "testType");
    }
    
    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(0), new AtomicLong(0), null);
        ObjectResult secondObjectResult = ObjectResult.of(objectStatus);
        assertEquals(objectResult.getSchema(), secondObjectResult.getSchema());
        assertEquals(objectResult.getName(), secondObjectResult.getName());
        assertEquals(objectResult.getType(), secondObjectResult.getType());
    }
   
    @Test
    public void testGetSummary() {
        String expectedSummary = "testSchema.testName[testType]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    // Test edge case when object status values are null
    @Test(expected = NullPointerException.class)
    public void testObjectResultOfWithNullValue() {
        ObjectStatus objectStatus = new ObjectStatus(null, null, null, null);
        ObjectResult.of(objectStatus);
    }

    // Test edge case when ObjectResult constructor parameters are null
    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorWithNullValue() {
        new ObjectResult(null, null, null);
    }
}
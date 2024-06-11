import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("testSchema", "testName", "testType");
    }

    @Test
    public void testObjectResultConstructor() {
        assertNotNull(objectResult);
    }

    @Test
    public void testObjectResultGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(5), new AtomicLong(10), "testType");
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals("testName", result.getName());
        assertEquals(5, result.getCount().get());
        assertEquals(10, result.getTotal().get());
        assertEquals("testType", result.getType());
    }

}
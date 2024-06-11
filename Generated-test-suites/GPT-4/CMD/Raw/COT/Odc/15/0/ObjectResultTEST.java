import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(10), new AtomicLong(100), "testStatus");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        String summary = objectResult.getSummary();
        assertEquals("schema1.name1[type1]", summary);
    }

    @Test
    public void testObjectResultConstructorEdgeCases() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getName());
        assertNull(objectResult.getType());
    }

    @Test
    public void testOfMethodEdgeCases() {
        ObjectStatus objectStatus = new ObjectStatus(null, null, null, null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNull(objectResult.getName());
        assertEquals(0, objectResult.getCount().get());
    }

    @Test
    public void testGetSummaryEdgeCases() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
        String summary = objectResult.getSummary();
        assertEquals(".[null]", summary);
    }
}
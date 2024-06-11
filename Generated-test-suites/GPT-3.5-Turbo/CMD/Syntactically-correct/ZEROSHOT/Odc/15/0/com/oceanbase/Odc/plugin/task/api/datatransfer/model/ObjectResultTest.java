import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");

        assertNotNull(objectResult);
        assertEquals("test_schema", objectResult.getSchema());
        assertEquals("test_name", objectResult.getName());
        assertEquals("test_type", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("test_name", new AtomicLong(100), new AtomicLong(200), "test_type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertNotNull(objectResult);
        assertEquals("test_name", objectResult.getName());
        assertEquals(new AtomicLong(100), objectResult.getCount());
        assertEquals(new AtomicLong(200), objectResult.getTotal());
        assertEquals("test_type", objectResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        String summary = objectResult.getSummary();

        assertEquals("test_schema.test_name[test_type]", summary);
    }
}
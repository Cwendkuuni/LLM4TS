
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertNotNull(objectResult);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), "type");
        ObjectResult result = ObjectResult.of(status);
        assertNotNull(result);
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String summary = objectResult.getSummary();
        assertNotNull(summary);
        assertEquals("schema.name[type]", summary);
    }
}

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertNotNull(result);
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertNotNull(result.getCount());
        assertNotNull(result.getTotal());
    }

    @Test
    public void testStaticMethodOf() {
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
        ObjectResult result = new ObjectResult("schema", "name", "type");
        String summary = result.getSummary();
        assertEquals("schema.name[type]", summary);
    }

}
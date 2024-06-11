import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import org.junit.Test;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setName("name");
        objectResult.setSchema("schema");
        objectResult.setType("type");
        objectResult.setStatus("status");
        objectResult.setCount(new AtomicLong(10));
        objectResult.setTotal(new AtomicLong(100));

        ObjectResult result = ObjectResult.of(objectResult);
        assertEquals("name", result.getName());
        assertEquals("schema", result.getSchema());
        assertEquals("type", result.getType());
        assertEquals("status", result.getStatus());
        assertEquals(10, result.getCount().get());
        assertEquals(100, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema");
        objectResult.setName("name");
        objectResult.setType("type");

        String summary = objectResult.getSummary();
        assertEquals("schema.name[type]", summary);
    }

}
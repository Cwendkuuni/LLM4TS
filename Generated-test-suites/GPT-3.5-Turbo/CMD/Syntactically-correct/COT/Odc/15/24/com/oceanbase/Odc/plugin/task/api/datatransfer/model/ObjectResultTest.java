import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        ObjectResult copiedObjectResult = ObjectResult.of(objectResult);
        
        assertEquals("schema", copiedObjectResult.getSchema());
        assertEquals("name", copiedObjectResult.getName());
        assertEquals("type", copiedObjectResult.getType());
        assertEquals(0, copiedObjectResult.getCount().get());
        assertEquals(0, copiedObjectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String expectedSummary = "schema.name[type]";
        
        assertEquals(expectedSummary, objectResult.getSummary());
    }
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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
        ObjectStatus objectStatus = new ObjectStatus("statusName", new AtomicLong(0), new AtomicLong(0), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("statusName", objectResult.getName());
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getCount());
        assertNull(objectResult.getTotal());
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
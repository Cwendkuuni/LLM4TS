import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResult() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("type");
        objectStatus.setStatus("status");
        objectStatus.setName("name");
        objectStatus.setSchema("schema");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getName(), objectResult.getName());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema");
        objectResult.setType("type");
        objectResult.setName("name");
        assertEquals(objectResult.getSummary(), "schema.name[type]");
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResult_NullValues() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultOf_Null() {
        ObjectResult objectResult = ObjectResult.of(null);
    }

}
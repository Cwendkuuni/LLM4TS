import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
        assertEquals(ObjectStatus.STATUS_INIT, objectResult.getStatus());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", 1L, 10L, null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("name1", objectResult.getName());
        assertEquals("name1", objectStatus.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals("type1", objectStatus.getType());
        assertEquals(1L, objectResult.getCount().get());
        assertEquals(10L, objectResult.getTotal().get());
        assertEquals(ObjectStatus.STATUS_INIT, objectResult.getStatus());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    // Edge cases testing

    @Test(expected = NullPointerException.class)
    public void testObjectResultNullConstructor() {
        new ObjectResult(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultNullType() {
        new ObjectResult("schema1", null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultNullSchema() {
        new ObjectResult(null, "name1", "type1");
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultNullObjectStatus() {
        ObjectResult objectResult = new ObjectResult();
        ObjectStatus objectStatus = null;
        objectResult.setObjectStatus(objectStatus);
    }

    // Exception handling testing

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultNullSchemaInConstructor() {
        new ObjectResult(null, "name1", "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultNullNameInConstructor() {
        new ObjectResult("schema1", null, "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultNullTypeInConstructor() {
        new ObjectResult("schema1", "name1", null);
    }

    @Test
    public void testObjectResultSetters() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema1");
        objectResult.setName("name1");
        objectResult.setType("type1");
        objectResult.setExportPaths(null);
        objectResult.setCount(new AtomicLong(1L));
        objectResult.setTotal(new AtomicLong(10L));
        objectResult.setStatus(ObjectStatus.STATUS_FAILED);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(1L, objectResult.getCount().get());
        assertEquals(10L, objectResult.getTotal().get());
        assertEquals(ObjectStatus.STATUS_FAILED, objectResult.getStatus());
    }

}
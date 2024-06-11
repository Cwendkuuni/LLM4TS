import org.junit.Test;
import org.junit.Assert.*;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import java.util.Arrays;
import java.util.AtomicLong;
import java.util.List;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "objectName1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("objectName1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("objectName2");
        objectStatus.setSchema("schema2");
        objectStatus.setType("type2");
        objectStatus.setStatus(ObjectStatus.Status.SUCCESS);
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("schema2", objectResult.getSchema());
        assertEquals("objectName2", objectResult.getName());
        assertEquals("type2", objectResult.getType());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals(new AtomicLong(10), objectResult.getCount());
        assertEquals(new AtomicLong(20), objectResult.getTotal());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema3", "objectName3", "type3");
        assertEquals("schema3.objectName3[type3]", objectResult.getSummary());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorNullSchema() {
        new ObjectResult(null, "objectName4", "type4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorNullName() {
        new ObjectResult("schema5", null, "type5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorNullType() {
        new ObjectResult("schema6", "objectName6", null);
    }

    @Test
    public void testObjectResultSetters() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema7");
        objectResult.setName("objectName7");
        objectResult.setType("type7");
        objectResult.setStatus(ObjectStatus.Status.FAILED);
        objectResult.setCount(new AtomicLong(5));
        objectResult.setTotal(new AtomicLong(15));
        assertEquals("schema7", objectResult.getSchema());
        assertEquals("objectName7", objectResult.getName());
        assertEquals("type7", objectResult.getType());
        assertEquals(ObjectStatus.Status.FAILED, objectResult.getStatus());
        assertEquals(new AtomicLong(5), objectResult.getCount());
        assertEquals(new AtomicLong(15), objectResult.getTotal());
    }

    @Test
    public void testObjectResultEqualsAndHashcode() {
        ObjectResult objectResult1 = new ObjectResult("schema1", "objectName1", "type1");
        ObjectResult objectResult2 = new ObjectResult("schema1", "objectName1", "type1");
        ObjectResult objectResult3 = new ObjectResult("schema1", "objectName2", "type1");
        ObjectResult objectResult4 = new ObjectResult("schema2", "objectName1", "type1");

        assertEquals(objectResult1, objectResult1);
        assertEquals(objectResult1, objectResult2);
        assertNotEquals(objectResult1, objectResult3);
        assertNotEquals(objectResult1, objectResult4);
    }

}
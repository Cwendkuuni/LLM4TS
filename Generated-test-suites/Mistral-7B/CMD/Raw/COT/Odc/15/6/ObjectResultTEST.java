import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("type1");
        objectStatus.setStatus(ObjectStatus.Status.SUCCESS);
        objectStatus.setName("table1");
        objectStatus.setSchema("schema1");
        objectStatus.setTotal(new java.lang.Long(123));
        objectStatus.setCount(new java.lang.Long(10));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals(new AtomicLong(123), objectResult.getTotal());
        assertEquals(new AtomicLong(10), objectResult.getCount());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema1");
        objectResult.setName("table1");
        objectResult.setType("type1");

        String expected = "schema1.table1[type1]";
        String actual = objectResult.getSummary();

        assertEquals(expected, actual);
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorInvalidSchema() {
        new ObjectResult(null, "table1", "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorInvalidName() {
        new ObjectResult("schema1", null, "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorInvalidType() {
        new ObjectResult("schema1", "table1", null);
    }

    @Test
    public void testGetSummaryEmptySchema() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("");
        objectResult.setName("table1");
        objectResult.setType("type1");

        String expected = "[table1][type1]";
        String actual = objectResult.getSummary();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSummaryEmptyName() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema1");
        objectResult.setName("");
        objectResult.setType("type1");

        String expected = "schema1.[type1]";
        String actual = objectResult.getSummary();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSummaryEmptyType() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema1");
        objectResult.setName("table1");
        objectResult.setType("");

        String expected = "schema1.table1[]";
        String actual = objectResult.getSummary();

        assertEquals(expected, actual);
    }

}
import org.junit.Test;
import org.junit.Assert.*;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";
        ObjectResult objectResult = new ObjectResult(schema, name, type);
        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals(0L, objectResult.getCount().get());
        assertEquals(0L, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema("testSchema");
        objectStatus.setName("testName");
        objectStatus.setType("testType");
        objectStatus.setCount(new AtomicLong(1));
        objectStatus.setTotal(new AtomicLong(1));
        ObjectStatus that = objectStatus;
        ObjectResult objectResult = ObjectResult.of(that);
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";
        ObjectResult objectResult = new ObjectResult(schema, name, type);
        String expected = String.format("%s.%s[%s]", schema, name, type);
        assertEquals(expected, objectResult.getSummary());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorNullName() {
        String schema = "testSchema";
        String name = null;
        String type = "testType";
        new ObjectResult(schema, name, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorNullSchema() {
        String schema = null;
        String name = "testName";
        String type = "testType";
        new ObjectResult(schema, name, type);
    }

    @Test
    public void testObjectResultGetExportPaths() {
        List<URL> exportPaths = null;
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(exportPaths);
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultSetExportPaths() {
        List<URL> exportPaths = java.util.Arrays.asList(new URL("testPath1"), new URL("testPath2"));
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
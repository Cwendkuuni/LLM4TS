import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import java.util.ArrayList;
import java.util.List;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertNotNull(objectResult);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.INIT, objectResult.getStatus());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("type1");
        objectStatus.setStatus(ObjectStatus.Status.SUCCESS);
        objectStatus.setName("name1");
        objectStatus.setSchema("schema1");
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructionWithNullName() {
        new ObjectResult(null, "name1", "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructionWithNullSchema() {
        new ObjectResult("schema1", null, "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructionWithNullType() {
        new ObjectResult("schema1", "name1", null);
    }

    @Test
    public void testObjectResultSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = new ArrayList<>();
        URL url = new URL("http://localhost:8080/exportPath");
        exportPaths.add(url);
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
        assertTrue(objectResult.getExportPaths().contains(url));
    }

}
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

import static com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus.*;

public class ObjectResultTest {

    @Test
    public void testObjectResult() {
        ObjectResult objectResult = new ObjectResult();
        assertEquals("", objectResult.getSchema());
        assertEquals("", objectResult.getName());
        assertEquals("", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.AVAILABLE, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema");
        objectResult.setName("name");
        objectResult.setType("type");

        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryWithNullSchema() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema(null);
        objectResult.setName("name");
        objectResult.setType("type");

        objectResult.getSummary();
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryWithNullName() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema");
        objectResult.setName(null);
        objectResult.setType("type");

        objectResult.getSummary();
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryWithNullType() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema");
        objectResult.setName("name");
        objectResult.setType(null);

        objectResult.getSummary();
    }

    @Test
    public void testObjectResultFromObjectStatus() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema("schema");
        objectStatus.setName("name");
        objectStatus.setType("type");
        objectStatus.setStatus(AVAILABLE);
        objectStatus.setCount(new AtomicLong(1));
        objectStatus.setTotal(new AtomicLong(1));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("schema.name[type]", objectResult.getSummary());
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(AVAILABLE, objectResult.getStatus());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(1, objectResult.getTotal().get());
    }

    @Test
    public void testGetExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(null);

        assertNull(objectResult.getExportPaths());

        List<URL> exportPaths = new java.util.ArrayList<>();
        URL url = new URL("http://example.com/export1.csv");
        exportPaths.add(url);
        objectResult.setExportPaths(exportPaths);

        assertEquals(1, objectResult.getExportPaths().size());
        assertEquals("http://example.com/export1.csv", objectResult.getExportPaths().get(0).toString());
    }
}

//
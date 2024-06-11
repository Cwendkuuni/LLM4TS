import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");

        assertNotNull(objectResult);
        assertEquals("test_schema", objectResult.getSchema());
        assertEquals("test_name", objectResult.getName());
        assertEquals("test_type", objectResult.getType());
        assertEquals(0L, objectResult.getStatus().get());
        assertEquals(0L, objectResult.getCount().get());
        assertEquals(0L, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("test_name", new AtomicLong(10), new AtomicLong(20), "test_type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertNotNull(objectResult);
        assertEquals("test_type", objectResult.getType());
        assertEquals("test_name", objectResult.getName());
        assertEquals(10L, objectResult.getCount().get());
        assertEquals(20L, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        String expectedSummary = "test_schema.test_name[test_type]";

        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult("test_schema", "test_name", "test_type");
        ArrayList<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("https://example.com"));

        objectResult.setExportPaths(exportPaths);
        
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

}
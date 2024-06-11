import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertNotNull(objectResult);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertNotNull(objectResult.getTotal());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), "type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals("name", objectResult.getName());
        assertEquals(new AtomicLong(10), objectResult.getCount());
        assertEquals(new AtomicLong(20), objectResult.getTotal());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String expectedSummary = "schema.name[type]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testExportPathsNull() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testExportPathsNotNull() {
        List<URL> exportPaths = Arrays.asList(new URL("https://example.com"));
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        objectResult.setExportPaths(exportPaths);
        assertNotNull(objectResult.getExportPaths());
        assertEquals(1, objectResult.getExportPaths().size());
    }

}
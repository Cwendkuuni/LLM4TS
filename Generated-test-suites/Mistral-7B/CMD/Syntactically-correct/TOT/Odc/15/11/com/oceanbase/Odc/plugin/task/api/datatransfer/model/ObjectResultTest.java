import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.commons.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unchecked")
public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        // Test with valid arguments
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());

        // Test with null arguments
        Assertions.assertThrows(NullPointerException.class, () -> new ObjectResult(null, "name1", "type1"));
        Assertions.assertThrows(NullPointerException.class, () -> new ObjectResult("schema1", null, "type1"));
        Assertions.assertThrows(NullPointerException.class, () -> new ObjectResult("schema1", "name1", null));
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("name1");
        objectStatus.setType("type1");
        objectStatus.setStatus(ObjectStatus.Status.SUCCESS);

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema1");
        objectResult.setName("name1");
        objectResult.setType("type1");

        String expectedSummary = "schema1.name1[type1]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testJsonIgnore() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);

        // Test getter
        assertNull(objectResult.getExportPaths());

        // Test setter
        List<URL> newExportPaths = new ArrayList<>();
        objectResult.setExportPaths(newExportPaths);
        assertEquals(newExportPaths, objectResult.getExportPaths());

        // Test reflection access
        ReflectionUtils.makeAccessible(ObjectResult.class.getDeclaredField("exportPaths"));
        Object result = ReflectionUtils.getFieldValue(objectResult, "exportPaths");
        assertEquals(newExportPaths, result);
    }

    @Test
    public void testMethodExecution() {
        ObjectResult objectResult = new ObjectResult();

        Executable executable = () -> objectResult.getExportPaths();
        assertThrows(NullPointerException.class, executable);
    }
}
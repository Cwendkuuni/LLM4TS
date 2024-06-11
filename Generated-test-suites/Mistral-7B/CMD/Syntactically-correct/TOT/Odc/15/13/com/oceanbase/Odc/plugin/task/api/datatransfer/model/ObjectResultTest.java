import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.runner.JUnit4;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        // Valid construction
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertNotNull(objectResult);
        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultOfObjectStatus() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(1), new AtomicLong(2), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testJsonIgnoreGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        assertThrows(IllegalAccessException.class, () -> objectResult.getExportPaths());
    }

    @Test
    public void testNegativeSchema() {
        Executable executable = () -> new ObjectResult(null, "testName", "testType");
        assertThrows(NullPointerException.class, executable);
    }

    @Test
    public void testEmptySchema() {
        Executable executable = () -> new ObjectResult("", "testName", "testType");
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void testEmptyName() {
        Executable executable = () -> new ObjectResult("testSchema", "", "testType");
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void testEmptyType() {
        Executable executable = () -> new ObjectResult("testSchema", "testName", "");
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void testExportPathsIsNull() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        List<URL> exportPaths = null;
        ObjectResult newObjectResult = new ObjectResult(objectResult).setExportPaths(exportPaths);
        assertNotNull(newObjectResult);
        assertEquals(objectResult.getSummary(), newObjectResult.getSummary());
        assertThrows(NullPointerException.class, () -> newObjectResult.getExportPaths());
    }

    @Test
    public void testExportPathsEmpty() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        List<URL> exportPaths = Collections.emptyList();
        ObjectResult newObjectResult = new ObjectResult(objectResult).setExportPaths(exportPaths);
        assertNotNull(newObjectResult);
        assertEquals(objectResult.getSummary(), newObjectResult.getSummary());
        assertEquals(0, newObjectResult.getExportPaths().size());
    }
}
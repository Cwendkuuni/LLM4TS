import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObjectResultTest {

    @Test
    void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "tableName1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("tableName1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.UNPROCESSED, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(10), new AtomicLong(20), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("name1", objectResult.getName());
        assertEquals(ObjectStatus.Status.UNPROCESSED, objectResult.getStatus());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "tableName1", "type1");
        assertEquals("schema1.tableName1[type1]", objectResult.getSummary());
    }

    @Test
    void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1"), new URL("http://localhost:8080/export2"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    void testJsonIgnore() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1"), new URL("http://localhost:8080/export2"));
        objectResult.setExportPaths(exportPaths);
        ObjectResult clone = ObjectResult.of(objectResult);
        assertNull(clone.getExportPaths());
    }

    @Test
    void testNegativeScenario() {
        ObjectResult objectResult = new ObjectResult();
        assertThrows(NullPointerException.class, () -> objectResult.setSchema(null));
        assertThrows(IllegalArgumentException.class, () -> objectResult.setName(null));
        assertThrows(IllegalArgumentException.class, () -> objectResult.setType(null));
        assertThrows(IllegalArgumentException.class, () -> objectResult.setCount(null));
        assertThrows(IllegalArgumentException.class, () -> objectResult.setTotal(null));
        assertThrows(IllegalArgumentException.class, () -> objectResult.setStatus(null));
        assertThrows(IllegalArgumentException.class, () -> objectResult.setExportPaths(null));
    }

    @Test
    void testSettersAndGetters() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema1");
        objectResult.setName("tableName1");
        objectResult.setType("type1");
        objectResult.setStatus(ObjectStatus.Status.PROCESSING);
        objectResult.setCount(new AtomicLong(123));
        objectResult.setTotal(new AtomicLong(456));
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1"), new URL("http://localhost:8080/export2"));
        objectResult.setExportPaths(exportPaths);

        ObjectResult clone = new ObjectResult();
        ReflectionUtils.setFieldValue(clone, "schema", "schema1");
        ReflectionUtils.setFieldValue(clone, "name", "tableName1");
        ReflectionUtils.setFieldValue(clone, "type", "type1");
        ReflectionUtils.setFieldValue(clone, "status", ObjectStatus.Status.PROCESSING.getCode());
        ReflectionUtils.setFieldValue(clone, "count", new AtomicLong(123));
        ReflectionUtils.setFieldValue(clone, "total", new AtomicLong(456));
        ReflectionUtils.setFieldValue(clone, "exportPaths", Collections.unmodifiableList(exportPaths));

        assertEquals(objectResult, clone);
    }
}
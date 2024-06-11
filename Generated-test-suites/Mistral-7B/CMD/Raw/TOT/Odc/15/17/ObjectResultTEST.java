import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.platform.commons.util.ReflectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObjectResultTest {

    @Test
    void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.STATUS_NONE, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("name1");
        objectStatus.setType("type1");
        objectStatus.setStatus(ObjectStatus.STATUS_FAILED);
        objectStatus.setSchema("schema1");
        objectStatus.setCount(new AtomicLong(1));
        objectStatus.setTotal(new AtomicLong(10));

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.STATUS_FAILED, objectResult.getStatus());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(10, objectResult.getTotal().get());
    }

    @Test
    void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        String summary = objectResult.getSummary();
        String expectedSummary = "schema1.name1[type1]";
        assertEquals(expectedSummary, summary);
    }

    @Test
    void testObjectResultWithNullArguments() {
        assertThrows(NullPointerException.class, () -> new ObjectResult(null, null, null));
        assertThrows(NullPointerException.class, () -> new ObjectResult("", null, null));
        assertThrows(NullPointerException.class, () -> new ObjectResult(null, "", null));

        ObjectResult objectResult = new ObjectResult(null, "name1", "type1");
        assertNull(objectResult.getSchema());

        objectResult = new ObjectResult("schema1", null, "type1");
        assertNull(objectResult.getName());

        objectResult = new ObjectResult("schema1", "name1", null);
        assertNull(objectResult.getType());
    }

    @Test
    void testObjectResultSetters() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = Collections.singletonList(new URL("http://test.com/export1.csv"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());

        objectResult.setStatus(ObjectStatus.STATUS_SUCCEEDED);
        assertEquals(ObjectStatus.STATUS_SUCCEEDED, objectResult.getStatus());

        objectResult.setName("name2");
        assertEquals("name2", objectResult.getName());

        objectResult.setType("type2");
        assertEquals("type2", objectResult.getType());

        objectResult.setSchema("schema2");
        assertEquals("schema2", objectResult.getSchema());

        objectResult.setCount(new AtomicLong(5));
        assertEquals(5, objectResult.getCount().get());

        objectResult.setTotal(new AtomicLong(20));
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    void testJsonIgnore() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = Collections.singletonList(new URL("http://test.com/export1.csv"));
        objectResult.setExportPaths(exportPaths);

        // Test via reflection as JSONIgnore annotation does not affect getters in JUnit 4
        Object result = ReflectionUtils.invokeMethod(objectResult, "getExportPaths", new Class[0][0]);
        assertNull(result);
    }

    @Test
    void testObjectResultNegativeCount() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setCount(new AtomicLong(-1));
        assertThrows(IllegalArgumentException.class, () -> objectResult.getCount());
    }

    @Test
    void testObjectResultNegativeTotal() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setTotal(new AtomicLong(-1));
        assertThrows(IllegalArgumentException.class, () -> objectResult.getTotal());
    }

    @Test
    void testObjectResultNullTotal() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setTotal(null);
        assertNull(objectResult.getTotal());
    }

    @Test
    void testObjectResultNullCount() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setCount(null);
        assertNull(objectResult.getCount());
    }

    @Test
    void testObjectResultEmptyExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setExportPaths(Collections.emptyList());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    void testObjectResultNonEmptyExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = Stream.of(
                        new URL("http://test.com/export1.csv"),
                        new URL("http://test.com/export2.csv")
                ).collect(Collectors.toList());
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
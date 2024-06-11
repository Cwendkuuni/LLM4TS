import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjectResultTest {

    @Test
    void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertAll(
                () -> assertEquals("schema1", objectResult.getSchema()),
                () -> assertEquals("name1", objectResult.getName()),
                () -> assertEquals("type1", objectResult.getType()),
                () -> assertNotNull(objectResult.getStatus()),
                () -> assertNotNull(objectResult.getCount()),
                () -> assertNotNull(objectResult.getTotal())
        );
    }

    @Test
    void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("name1");
        objectStatus.setSchema("schema1");
        objectStatus.setType("type1");
        objectStatus.setCount(new AtomicLong(1));
        objectStatus.setTotal(new AtomicLong(10));

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertAll(
                () -> assertEquals("schema1", objectResult.getSchema()),
                () -> assertEquals("name1", objectResult.getName()),
                () -> assertEquals("type1", objectResult.getType()),
                () -> assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus()),
                () -> assertEquals(1, objectResult.getCount().get()),
                () -> assertEquals(10, objectResult.getTotal().get())
        );
    }

    @Test
    void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    void testJsonIgnore() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertNull(ReflectionUtils.getFieldValue(objectResult, "exportPaths"));
    }

    @Test
    void testEmptyListExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setExportPaths(Collections.emptyList());
        assertTrue(objectResult.getExportPaths().isEmpty());
    }

    @Test
    void testSingleExportPath() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export/path/1"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(1, objectResult.getExportPaths().size());
        assertEquals("http://localhost:8080/export/path/1", objectResult.getExportPaths().get(0).toString());
    }

    @Test
    void testNullExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setExportPaths(null);
        assertNull(objectResult.getExportPaths());
    }
}
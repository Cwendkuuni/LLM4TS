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
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.AVAILABLE, objectResult.getStatus());
        assertEquals(0L, objectResult.getCount().get());
        assertEquals(0L, objectResult.getTotal().get());
    }

    @Test
    void testObjectResultStaticCreation() {
        ObjectStatus objectStatus = new ObjectStatus("testName", 1L, 2L, null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("testName", objectResult.getName());
        assertEquals(ObjectStatus.Status.AVAILABLE, objectResult.getStatus());
        assertEquals(1L, objectResult.getCount().get());
        assertEquals(2L, objectResult.getTotal().get());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

    @Test
    void testJsonIgnore() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        objectResult.setExportPaths(Collections.singletonList(new URL("testUrl")));
        ReflectionUtils.setFieldValue(objectResult, "exportPaths", null);
        assertNull(objectResult.getExportPaths());
    }

    @Test
    void testInvalidObjectResultConstruction() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ObjectResult(null, "testName", "testType"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ObjectResult("testSchema", null, "testType"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ObjectResult("testSchema", "testName", null));
    }

    @Test
    void testSetType() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType1");
        objectResult.setType("testType2");
        assertEquals("testType2", objectResult.getType());
    }

    @Test
    void testSetStatus() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        objectResult.setStatus(ObjectStatus.Status.FAILED);
        assertEquals(ObjectStatus.Status.FAILED, objectResult.getStatus());
    }

    @Test
    void testSetName() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName1", "testType");
        objectResult.setName("testName2");
        assertEquals("testName2", objectResult.getName());
    }

    @Test
    void testSetSchema() {
        ObjectResult objectResult = new ObjectResult("testSchema1", "testName", "testType");
        objectResult.setSchema("testSchema2");
        assertEquals("testSchema2", objectResult.getSchema());
    }

    @Test
    void testSetCount() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        objectResult.setCount(new AtomicLong(10L));
        assertEquals(10L, objectResult.getCount().get());
    }

    @Test
    void testSetTotal() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        objectResult.setTotal(new AtomicLong(20L));
        assertEquals(20L, objectResult.getTotal().get());
    }

    @Test
    void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        List<URL> exportPaths = Arrays.asList(new URL("testUrl1"), new URL("testUrl2"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(JUnit4.class)
public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
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
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("tableName1");
        objectStatus.setType("type1");
        objectStatus.setSchema("schema1");
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));
        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("schema1", objectResult.getSchema());
        assertEquals("tableName1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.UNPROCESSED, objectResult.getStatus());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType("type1");
        objectResult.setStatus(ObjectStatus.Status.PROCESSED);
        objectResult.setName("tableName1");
        objectResult.setSchema("schema1");
        objectResult.setCount(new AtomicLong(10));
        objectResult.setTotal(new AtomicLong(20));

        String expectedSummary = "schema1.tableName1[type1]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testNullValues() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType(null);
        objectResult.setStatus(null);
        objectResult.setName(null);
        objectResult.setSchema(null);
        objectResult.setCount(null);
        objectResult.setTotal(null);

        assertNull(objectResult.getType());
        assertNull(objectResult.getStatus());
        assertNull(objectResult.getName());
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getCount());
        assertNull(objectResult.getTotal());
    }

    @Test
    public void testEmptyExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(Collections.emptyList());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testExportPathsWithOneElement() {
        ObjectResult objectResult = new ObjectResult();
        URL url = new URL("file:/test/exportPath.csv");
        objectResult.setExportPaths(Arrays.asList(url));
        assertEquals(1, objectResult.getExportPaths().size());
        assertEquals(url, objectResult.getExportPaths().get(0));
    }

    @Test
    public void testObjectResultSettersAndGetters() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType("type1");
        objectResult.setStatus(ObjectStatus.Status.PROCESSED);
        objectResult.setName("tableName1");
        objectResult.setSchema("schema1");
        objectResult.setCount(new AtomicLong(10));
        objectResult.setTotal(new AtomicLong(20));
        objectResult.setExportPaths(Arrays.asList(new URL("file:/test/exportPath1.csv"), new URL("file:/test/exportPath2.csv")));

        assertEquals("type1", objectResult.getType());
        assertEquals(ObjectStatus.Status.PROCESSED, objectResult.getStatus());
        assertEquals("tableName1", objectResult.getName());
        assertEquals("schema1", objectResult.getSchema());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
        assertEquals(2, objectResult.getExportPaths().size());
        assertEquals(new URL("file:/test/exportPath1.csv"), objectResult.getExportPaths().get(0));
        assertEquals(new URL("file:/test/exportPath2.csv"), objectResult.getExportPaths().get(1));
    }
}
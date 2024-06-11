import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(JUnitPlatform.class)
public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertNotNull(objectResult);
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(2), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummaryNullValues() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
        assertNull(objectResult.getSummary());
    }

    @Test
    public void testGetSummaryEmptyStringValues() {
        ObjectResult objectResult = new ObjectResult("", "", "");
        assertEquals("", objectResult.getSummary());
    }

    @Test
    public void testGetSummaryValuesWithSpaces() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testGetSummaryValuesWithSpecialCharacters() {
        ObjectResult objectResult = new ObjectResult("schema1%", "name1!", "type1@");
        assertEquals("schema1%name1![type1@]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultSetters() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType("type1");
        objectResult.setStatus(ObjectStatus.Status.SUCCESS);
        objectResult.setName("name1");
        objectResult.setSchema("schema1");
        objectResult.setCount(new AtomicLong(3));
        objectResult.setTotal(new AtomicLong(5));

        assertEquals("type1", objectResult.getType());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals("name1", objectResult.getName());
        assertEquals("schema1", objectResult.getSchema());
        assertEquals(new AtomicLong(3), objectResult.getCount());
        assertEquals(new AtomicLong(5), objectResult.getTotal());
    }

    @Test
    public void testObjectResultGetters() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType("type1");
        objectResult.setStatus(ObjectStatus.Status.SUCCESS);
        objectResult.setName("name1");
        objectResult.setSchema("schema1");
        objectResult.setCount(new AtomicLong(3));
        objectResult.setTotal(new AtomicLong(5));

        assertEquals("type1", objectResult.getType());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals("name1", objectResult.getName());
        assertEquals("schema1", objectResult.getSchema());
        assertEquals(new AtomicLong(3), objectResult.getCount());
        assertEquals(new AtomicLong(5), objectResult.getTotal());
    }

    @Test
    public void testObjectResultEmptyListExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultNullExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(null);
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultEmptyListExportPathsSetter() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(Collections.emptyList());
        assertNotNull(objectResult.getExportPaths());
        assertEquals(0, objectResult.getExportPaths().size());
    }

    @Test
    public void testObjectResultListExportPathsGetter() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/test1"), new URL("http://localhost:8080/test2"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(2, objectResult.getExportPaths().size());
    }

    @Test
    public void testObjectResultListExportPathsSetterWithNullValue() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = Collections.singletonList(new URL("http://localhost:8080/test1"));
        assertThrows(NullPointerException.class, () -> objectResult.setExportPaths(null));
        objectResult.setExportPaths(exportPaths);
        assertNotNull(objectResult.getExportPaths());
        assertEquals(1, objectResult.getExportPaths().size());
    }
}
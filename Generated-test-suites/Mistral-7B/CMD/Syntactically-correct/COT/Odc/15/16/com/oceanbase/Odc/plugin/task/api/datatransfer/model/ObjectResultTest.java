import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ObjectResultTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    // Test constructor
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.AVAILABLE, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    // Test of static method
    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = mock(ObjectStatus.class);
        when(objectStatus.getType()).thenReturn("type");
        when(objectStatus.getStatus()).thenReturn(ObjectStatus.Status.AVAILABLE);
        when(objectStatus.getName()).thenReturn("name");
        when(objectStatus.getSchema()).thenReturn("schema");
        when(objectStatus.getCount()).thenReturn(new AtomicLong(1));
        when(objectStatus.getTotal()).thenReturn(new AtomicLong(1));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.AVAILABLE, objectResult.getStatus());
        assertEquals(new AtomicLong(1), objectResult.getCount());
        assertEquals(new AtomicLong(1), objectResult.getTotal());
    }

    // Test getSummary method
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    // Test empty constructor
    @Test
    public void testEmptyConstructor() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getName());
        assertNull(objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.AVAILABLE, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    // Edge case tests

    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorNullName() {
        new ObjectResult(null, "name", "type");
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorNullType() {
        new ObjectResult("schema", null, "type");
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorNullSchema() {
        new ObjectResult(null, "name", null);
    }

    @Test
    public void testSetExportPathsNull() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertNull(objectResult.getExportPaths());
        objectResult.setExportPaths(null);
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testSetExportPathsEmptyList() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testSetExportPathsNonEmptyList() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://test.com/export/path1"));
        exportPaths.add(new URL("http://test.com/export/path2"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExportPathsNullURL() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(null);
        objectResult.setExportPaths(exportPaths);
    }

}
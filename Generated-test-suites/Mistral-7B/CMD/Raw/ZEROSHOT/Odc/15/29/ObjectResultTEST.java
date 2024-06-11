import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ObjectResultTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @After
    public void teardown() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(ObjectStatus.STATUS_NONE, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOfObjectStatus() {
        ObjectStatus objectStatus = mock(ObjectStatus.class);
        when(objectStatus.getType()).thenReturn("type1");
        when(objectStatus.getStatus()).thenReturn(ObjectStatus.STATUS_IN_PROGRESS);
        when(objectStatus.getName()).thenReturn("name1");
        when(objectStatus.getSchema()).thenReturn("schema1");
        when(objectStatus.getCount()).thenReturn(new AtomicLong(10));
        when(objectStatus.getTotal()).thenReturn(new AtomicLong(100));

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(ObjectStatus.STATUS_IN_PROGRESS, objectResult.getStatus());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(100, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost:8080/export1"));
        exportPaths.add(new URL("http://localhost:8080/export2"));
        objectResult.setExportPaths(exportPaths);

        assertEquals(2, objectResult.getExportPaths().size());
        assertTrue(objectResult.getExportPaths().contains(new URL("http://localhost:8080/export1")));
        assertTrue(objectResult.getExportPaths().contains(new URL("http://localhost:8080/export2")));
    }

    @Test
    public void testJsonIgnoreGetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost:8080/export1"));
        exportPaths.add(new URL("http://localhost:8080/export2"));
        objectResult.setExportPaths(exportPaths);

        assertNull(objectMapper.convertValue(objectResult, ObjectResult.class).getExportPaths());
    }
}
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import java.util.Arrays;
import java.util.AtomicLong;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectResult = new ObjectResult();
        objectStatus = mock(ObjectStatus.class);
    }

    // Test constructor
    @Test
    public void testObjectResultConstruction() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(ObjectStatus.UNCHANGED, result.getStatus());
        assertEquals(new AtomicLong(0), result.getCount());
        assertEquals(new AtomicLong(0), result.getTotal());
    }

    // Test static method "of"
    @Test
    public void testObjectResultOf() {
        when(objectStatus.getType()).thenReturn("type1");
        when(objectStatus.getStatus()).thenReturn(ObjectStatus.UNCHANGED);
        when(objectStatus.getName()).thenReturn("name1");
        when(objectStatus.getSchema()).thenReturn("schema1");
        when(objectStatus.getCount()).thenReturn(new AtomicLong(10));
        when(objectStatus.getTotal()).thenReturn(new AtomicLong(20));

        ObjectResult result = ObjectResult.of(objectStatus);

        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(ObjectStatus.UNCHANGED, result.getStatus());
        assertEquals(new AtomicLong(10), result.getCount());
        assertEquals(new AtomicLong(20), result.getTotal());

        verify(objectStatus).getType();
        verify(objectStatus).getStatus();
        verify(objectStatus).getName();
        verify(objectStatus).getSchema();
        verify(objectStatus, times(2)).getCount();
        verify(objectStatus, times(2)).getTotal();
    }

    // Test getSummary method
    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult();
        result.setSchema("schema1");
        result.setName("name1");
        result.setType("type1");

        assertEquals("schema1.name1[type1]", result.getSummary());
    }

    // Test getter for exportPaths
    @Test
    public void testGetterForExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1"), new URL("http://localhost:8080/export2"));
        objectResult.setExportPaths(exportPaths);

        assertEquals(exportPaths, objectResult.getExportPaths());
        verifyNoMoreInteractions(objectResult);
    }

    // Test constructor with ObjectStatus argument and getter for exportPaths
    @Test
    public void testObjectResultConstructionWithObjectStatusAndGetterForExportPaths() {
        ObjectStatus objectStatus = mock(ObjectStatus.class);
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1"), new URL("http://localhost:8080/export2"));
        when(objectStatus.getType()).thenReturn("type1");
        when(objectStatus.getStatus()).thenReturn(ObjectStatus.UNCHANGED);
        when(objectStatus.getName()).thenReturn("name1");
        when(objectStatus.getSchema()).thenReturn("schema1");
        when(objectStatus.getCount()).thenReturn(new AtomicLong(10));
        when(objectStatus.getTotal()).thenReturn(new AtomicLong(20));

        ObjectResult objectResult = new ObjectResult(objectStatus);

        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(ObjectStatus.UNCHANGED, objectResult.getStatus());
        assertEquals(new AtomicLong(10), objectResult.getCount());
        assertEquals(new AtomicLong(20), objectResult.getTotal());
        assertEquals(exportPaths, objectResult.getExportPaths());

        verify(objectStatus).getType();
        verify(objectStatus).getStatus();
        verify(objectStatus).getName();
        verify(objectStatus).getSchema();
        verify(objectStatus, times(2)).getCount();
        verify(objectStatus, times(2)).getTotal();
        verifyNoInteractions(objectResult);
    }

    // Test edge cases: empty constructor
    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultEmptyConstructor() {
        new ObjectResult();
    }

    // Test edge cases: null ObjectStatus argument
    @Test(expected = NullPointerException.class)
    public void testObjectResultNullObjectStatusArgument() {
        new ObjectResult(null);
    }

    // Test edge cases: null type, schema, name, and exportPaths
    @Test(expected = NullPointerException.class)
    public void testObjectResultNullTypeSchemaNameAndExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType(null);
        objectResult.setSchema(null);
        objectResult.setName(null);
        objectResult.setExportPaths(null);
    }
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {
    
    @Test
    public void testObjectResultConstructor() {
        String schema = "Schema";
        String name = "Name";
        String type = "Type";
        
        ObjectResult objectResult = new ObjectResult(schema, name, type);
        
        assertNotNull(objectResult);
        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }
    
    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("Name", new AtomicLong(1), new AtomicLong(2), "Type");
        
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
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("Schema", "Name", "Type");
        
        String expectedSummary = "Schema.Name[Type]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }
    
    @Test
    public void testExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://www.example.com"));
        
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(exportPaths);
        
        assertEquals(1, objectResult.getExportPaths().size());
        assertEquals("http://www.example.com", objectResult.getExportPaths().get(0).toString());
    }
}
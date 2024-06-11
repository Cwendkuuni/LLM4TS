package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectResultTest {
    
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }
    
    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals("name", objectResult.getName());
    }
    
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String summary = objectResult.getSummary();
        assertEquals("schema.name[type]", summary);
    }
    
    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getExportPaths());
        
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://example.com"));
        objectResult.setExportPaths(exportPaths);
        
        assertEquals(1, objectResult.getExportPaths().size());
        assertEquals(new URL("http://example.com"), objectResult.getExportPaths().get(0));
    }
}
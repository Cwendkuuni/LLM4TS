package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultOf() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        ObjectResult result = ObjectResult.of(objectResult);
        
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertNotNull(result.getCount());
        assertNotNull(result.getTotal());
        assertNull(result.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    // Additional test cases for edge cases and exceptions can be added here
    
}
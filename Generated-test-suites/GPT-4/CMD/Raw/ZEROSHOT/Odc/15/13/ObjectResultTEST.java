
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {
    
    private ObjectResult objectResult;

    @Before
    public void setUp() {
        this.objectResult = new ObjectResult("schema", "name", "type");
    }
    
    @Test
    public void testNewObjectResult() {
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0L, objectResult.getCount().get());
        assertEquals(0L, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus obj = new ObjectStatus("name2", new AtomicLong(5), new AtomicLong(10), "status");
        obj.setSchema("schema2");
        obj.setType("type2");
        
        ObjectResult result = ObjectResult.of(obj);
        
        assertNotNull(result);
        assertEquals("name2", result.getName());
        assertEquals("type2", result.getType());
        assertEquals("status", result.getStatus());
        assertEquals("schema2", result.getSchema());
        assertEquals(5L, result.getCount().get());
        assertEquals(10L, result.getTotal().get());
    }
    
    @Test
    public void testGetSetExportPaths() {
        List<URL> paths = new ArrayList<>();
        
        objectResult.setExportPaths(paths);
        
        assertEquals(paths, objectResult.getExportPaths());
    }

    @Test
    public void testSummary() {
        String expectedString = "schema.name[type]";
        
        assertEquals(expectedString, objectResult.getSummary());
    }
}

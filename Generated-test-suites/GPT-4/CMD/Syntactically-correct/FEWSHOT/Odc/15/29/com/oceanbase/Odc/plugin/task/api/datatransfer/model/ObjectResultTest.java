
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectResultTest {
  
    @Test
    public void testObjectResult_ConstructorWithParams() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }
    
    @Test
    public void testObjectResult_ObjectResultOfMethod() {
        ObjectStatus that = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), "status");
        that.setSchema("schema");
        that.setType("type");
        
        ObjectResult objectResult = ObjectResult.of(that);

        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals("status", objectResult.getStatus());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }
    
    @Test
    public void testObjectResult_GetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }
    
    @Test
    public void testObjectResult_ExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getExportPaths());
        
        List<URL> urls = new ArrayList<>();
        urls.add(new URL("http://localhost:80"));
        objectResult.setExportPaths(urls);
        assertTrue(objectResult.getExportPaths().contains(new URL("http://localhost:80")));
    }
}

package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testAdditionalConstructor() {
        String schema = "shemaTest";
        String name = "nameTest";
        String type = "typeTest";
        ObjectResult objectResult = new ObjectResult(schema, name, type);
        
        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
        assertNull(objectResult.getUsage());
    }
    
    @Test
    public void testMethodOf() {
        String schema = "shemaTest";
        String name = "nameTest";
        String type = "typeTest";
        ObjectStatus objectStatus = new ObjectStatus(name, new AtomicLong(10), new AtomicLong(15), null);
        objectStatus.setType(type);
        objectStatus.setSchema(schema);
        
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        
        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(15, objectResult.getTotal().get());
        assertNull(objectResult.getUsage());
    }
    
    @Test
    public void testGetSummary() {
        String schema = "shemaTest";
        String name = "nameTest";
        String type = "typeTest";
        ObjectResult objectResult = new ObjectResult(schema, name, type);

        String expectedSummary = String.format("%s.%s[%s]", schema, name, type);
        assertEquals(expectedSummary, objectResult.getSummary());
    }
    
    @Test
    public void testExportPaths() {
        List<URL> paths = new ArrayList<>();
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(paths);
        
        assertEquals(paths, objectResult.getExportPaths());
    }
}
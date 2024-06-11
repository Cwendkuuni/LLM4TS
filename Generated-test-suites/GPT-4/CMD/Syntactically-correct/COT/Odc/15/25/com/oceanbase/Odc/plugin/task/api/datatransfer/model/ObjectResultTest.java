package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import java.net.URL;

public class ObjectResultTest {
  
    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }
  
    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        objectStatus.setType("type");
        objectStatus.setSchema("schema");
        ObjectResult result = ObjectResult.of(objectStatus);
        
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
    }
  
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String summary = objectResult.getSummary();
        assertEquals("schema.name[type]", summary);
    }

    // Here is a potential edge case where objectStatus passed to the of() method is null
    @Test(expected = NullPointerException.class)
    public void testOfMethodWithNullObjectStatus() {
        ObjectResult result = ObjectResult.of(null);
    }
  
    // Potential test case where we pass an empty list to setExportPaths
    @Test
    public void testSetGetExportPathsEmptyList() {
        List<URL> urlList = Collections.emptyList();
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        objectResult.setExportPaths(urlList);
        assertEquals(urlList, objectResult.getExportPaths());
    }
  
    // Potential test case where we pass a null list to setExportPaths
    @Test(expected = NullPointerException.class)
    public void testSetExportPathsNullList() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        objectResult.setExportPaths(null);
    }

}


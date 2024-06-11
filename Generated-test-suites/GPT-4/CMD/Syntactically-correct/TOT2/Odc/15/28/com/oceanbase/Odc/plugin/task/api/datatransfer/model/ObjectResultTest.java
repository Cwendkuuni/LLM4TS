package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class ObjectResultTest {
    
    private static ObjectResult objectResult;
    
    @BeforeClass
    public static void setup() {
        objectResult = new ObjectResult("schema1", "name1", "type1");
    }
        
    @Test
    public void testObjectResultConstruction() {
        assertNotNull(objectResult);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
    }
    
    @Test
    public void testOfObjectStatus() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("type2");
        objectStatus.setStatus("status2");
        objectStatus.setName("name2");
        objectStatus.setSchema("schema2");
        
        ObjectResult result = ObjectResult.of(objectStatus);
        
        assertNotNull(result);
        assertEquals("type2", result.getType());
        assertEquals("status2", result.getStatus());
        assertEquals("name2", result.getName());
        assertEquals("schema2", result.getSchema());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("schema1.name1[type1]", summary);
    }
         
    // Other tests for edge cases and exceptions would follow here...
}

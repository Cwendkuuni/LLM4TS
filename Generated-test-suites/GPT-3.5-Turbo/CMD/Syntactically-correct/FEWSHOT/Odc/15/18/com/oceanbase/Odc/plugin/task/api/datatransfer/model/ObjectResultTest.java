package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectResultTest {
    
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }
    
    @Test
    public void testObjectResultOf() {
        ObjectResult testObject = new ObjectResult("testSchema", "testName", "testType");
        ObjectResult objectResult = ObjectResult.of(testObject);
        
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }
    
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        String expectedSummary = "testSchema.testName[testType]";
        
        assertEquals(expectedSummary, objectResult.getSummary());
    }
}
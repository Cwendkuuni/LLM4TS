package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ObjectResultTest {
    
    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertNotNull(objectResult);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }
    
    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), "type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }
    
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }
}
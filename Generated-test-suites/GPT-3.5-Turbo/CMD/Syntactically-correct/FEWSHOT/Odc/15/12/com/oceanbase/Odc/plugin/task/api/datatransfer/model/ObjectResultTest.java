package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectResultTest {
    
    @Test
    public void testObjectResultConstructor() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
    }
    
    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        ObjectResult result = ObjectResult.of(status);
        assertEquals("name", result.getName());
        assertEquals(status.getCount().get(), result.getCount().get());
        assertEquals(status.getTotal().get(), result.getTotal().get());
    }
    
    @Test
    public void testSummary() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        String expectedSummary = "schema.name[type]";
        assertEquals(expectedSummary, result.getSummary());
    }
}
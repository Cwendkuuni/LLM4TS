package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectResultTest {
    
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objResult.getSchema());
        assertEquals("name", objResult.getName());
        assertEquals("type", objResult.getType());
        assertNotNull(objResult.getCount());
        assertNotNull(objResult.getTotal());
        assertNull(objResult.getExportPaths());
    }
    
    @Test
    public void testObjectResultOf() {
        ObjectStatus objStatus = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), "type");
        ObjectResult objResult = ObjectResult.of(objStatus);
        assertEquals("name", objResult.getName());
        assertEquals("type", objResult.getType());
        assertEquals(10, objResult.getCount().get());
        assertEquals(20, objResult.getTotal().get());
        assertNotNull(objResult.getSchema());
    }
    
    @Test
    public void testGetSummary() {
        ObjectResult objResult = new ObjectResult("schema", "name", "type");
        String summary = objResult.getSummary();
        assertEquals("schema.name[type]", summary);
    }
}
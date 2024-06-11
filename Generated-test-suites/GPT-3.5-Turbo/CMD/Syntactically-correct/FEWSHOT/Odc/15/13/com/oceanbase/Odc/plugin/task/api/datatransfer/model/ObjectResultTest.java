package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("testObject", new AtomicLong(10), new AtomicLong(20), null);
        ObjectResult objectResult = ObjectResult.of(status);
        assertEquals("testObject", objectResult.getName());
        assertEquals(10L, objectResult.getCount().get());
        assertEquals(20L, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        assertNotNull(objectResult.getExportPaths());
        objectResult.setExportPaths(null);
        assertNull(objectResult.getExportPaths());
    }

}
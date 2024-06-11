package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertNotNull(objectResult);
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
        assertEquals("type1", objectResult.getType());
        assertEquals("name1", objectResult.getName());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals("name1", objectResult.getName());
        assertEquals("name1", objectResult.getSummary());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
    }
}
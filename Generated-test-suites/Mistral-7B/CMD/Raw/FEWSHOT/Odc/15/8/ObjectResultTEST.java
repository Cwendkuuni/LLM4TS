package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        assertNotNull(objectResult.getName());
        assertNotNull(objectResult.getType());
        assertNotNull(objectResult.getSchema());
        assertNull(objectResult.getExportPaths());
        assertEquals("schema1.table1[int]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult.getName());
        assertNotNull(objectResult.getType());
        assertNotNull(objectResult.getSchema());
        assertNull(objectResult.getExportPaths());
        assertEquals("name1[int]", objectResult.getSummary());
        assertEquals("int", objectResult.getType());
        assertEquals("name1", objectResult.getName());
        assertEquals("null", objectResult.getStatus());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(1, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        assertEquals("schema1.table1[int]", objectResult.getSummary());
    }
}
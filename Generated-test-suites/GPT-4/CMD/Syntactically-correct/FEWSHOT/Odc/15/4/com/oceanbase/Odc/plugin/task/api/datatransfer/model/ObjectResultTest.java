package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {
    @Test
    public void testObjectResultInit() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
        assertNull(objectResult.getStatus());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(2), new AtomicLong(4), 'a');
        objectStatus.setType("type1");
        objectStatus.setSchema("schema1");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(2, objectResult.getCount().get());
        assertEquals(4, objectResult.getTotal().get());
        assertEquals(Character.valueOf('a'), objectResult.getStatus());
    }

    @Test
    public void testObjectResultGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
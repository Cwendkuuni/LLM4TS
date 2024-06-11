package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(10), new AtomicLong(5), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("name1", objectResult.getName());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals("type1", objectResult.getType());
        assertEquals(new AtomicLong(10), objectResult.getCount());
        assertEquals(new AtomicLong(5), objectResult.getTotal());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1.table1[type1]", objectResult.getSummary());
    }

    @Test
    public void testSettingExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://test.com/export1"));
        exportPaths.add(new URL("http://test.com/export2"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
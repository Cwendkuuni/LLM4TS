package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "STRING");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("STRING", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertNull(objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("NAME1", new AtomicLong(1), new AtomicLong(10), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("NAME1", objectResult.getName());
        assertEquals("NAME1", objectResult.getStatus().getName());
        assertEquals("STRING", objectResult.getType());
        assertEquals(1, objectResult.getCount().get());
        assertEquals(10, objectResult.getTotal().get());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "STRING");
        assertEquals("schema1.table1[STRING]", objectResult.getSummary());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "STRING");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("file:export1.csv"));
        exportPaths.add(new URL("file:export2.csv"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
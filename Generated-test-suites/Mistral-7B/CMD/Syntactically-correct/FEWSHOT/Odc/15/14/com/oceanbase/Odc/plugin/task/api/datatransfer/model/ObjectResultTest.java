package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        assertNotNull(objectResult);
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("int", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.UNPROCESSED, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("table1", new AtomicLong(10), new AtomicLong(15), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals("table1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("int", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.UNPROCESSED, objectResult.getStatus());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(15, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        assertEquals("schema1.table1[int]", objectResult.getSummary());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "int");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("file:export1.csv"));
        exportPaths.add(new URL("file:export2.csv"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
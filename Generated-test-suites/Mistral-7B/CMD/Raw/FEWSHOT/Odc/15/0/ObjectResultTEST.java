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
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "TYPE1");
        assertNotNull(objectResult.getType());
        assertNotNull(objectResult.getName());
        assertNotNull(objectResult.getSchema());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("NAME1", new AtomicLong(5), new AtomicLong(10), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult.getType());
        assertNotNull(objectResult.getName());
        assertNotNull(objectResult.getSchema());
        assertEquals(5, objectResult.getCount().get());
        assertEquals(10, objectResult.getTotal().get());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "TYPE1");
        assertEquals("schema1.table1[TYPE1]", objectResult.getSummary());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "TYPE1");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost:8080/export/path1"));
        exportPaths.add(new URL("http://localhost:8080/export/path2"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(2, objectResult.getExportPaths().size());
        assertEquals("http://localhost:8080/export/path1", objectResult.getExportPaths().get(0).toString());
        assertEquals("http://localhost:8080/export/path2", objectResult.getExportPaths().get(1).toString());
    }
}
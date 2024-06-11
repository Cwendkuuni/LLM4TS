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
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertNotNull(objectResult.getSchema());
        assertEquals("schema1", objectResult.getSchema());
        assertNotNull(objectResult.getName());
        assertEquals("table1", objectResult.getName());
        assertNotNull(objectResult.getType());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult.getSchema());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertNotNull(objectResult.getCount());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertNotNull(objectResult.getTotal());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1.table1[type1]", objectResult.getSummary());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost:8080/export1"));
        exportPaths.add(new URL("http://localhost:8080/export2"));
        objectResult.setExportPaths(exportPaths);
        assertNotNull(objectResult.getExportPaths());
        assertEquals(2, objectResult.getExportPaths().size());
    }

    @Test
    public void testGetExportPathsWhenNull() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertNull(objectResult.getExportPaths());
    }
}
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "table1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.CREATED, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("type1");
        objectStatus.setStatus(ObjectStatus.Status.SUCCEEDED);
        objectStatus.setName("table1");
        objectStatus.setSchema("schema1");
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("schema1", objectResult.getSchema());
        assertEquals("table1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCEEDED, objectResult.getStatus());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
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
        exportPaths.add(new URL("file1.csv"));
        exportPaths.add(new URL("file2.csv"));
        objectResult.setExportPaths(exportPaths);

        assertEquals(2, objectResult.getExportPaths().size());
        assertTrue(objectResult.getExportPaths().contains(new URL("file1.csv")));
        assertTrue(objectResult.getExportPaths().contains(new URL("file2.csv")));
    }
}
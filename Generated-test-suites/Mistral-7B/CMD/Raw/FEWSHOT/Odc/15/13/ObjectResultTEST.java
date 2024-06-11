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
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("type1");
        objectStatus.setStatus(ObjectStatus.Status.SUCCESS);
        objectStatus.setName("name1");
        objectStatus.setSchema("schema1");
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost/export1"));
        exportPaths.add(new URL("http://localhost/export2"));
        objectResult.setExportPaths(exportPaths);

        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testJsonIgnore() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost/export1"));
        exportPaths.add(new URL("http://localhost/export2"));
        objectResult.setExportPaths(exportPaths);

        assertNull(objectResult.getExportPaths());
    }
}
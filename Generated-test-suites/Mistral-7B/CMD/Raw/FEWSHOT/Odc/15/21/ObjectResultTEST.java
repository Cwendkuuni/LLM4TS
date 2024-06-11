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
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertNotNull(objectResult);
        assertEquals("testName", objectResult.getName());
        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
        assertEquals("testType", objectResult.getType());
        assertEquals("testSchema", objectResult.getSchema());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("testName");
        objectStatus.setType("testType");
        objectStatus.setSchema("testSchema");
        objectStatus.setCount(new AtomicLong(1));
        objectStatus.setTotal(new AtomicLong(1));

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals("testName", objectResult.getName());
        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
        assertEquals("testType", objectResult.getType());
        assertEquals("testSchema", objectResult.getSchema());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultGettersSetters() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://test.com/export/path"));
        objectResult.setExportPaths(exportPaths);

        assertEquals("testName", objectResult.getName());
        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
        assertEquals("testType", objectResult.getType());
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
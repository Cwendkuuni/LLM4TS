package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("testName", objectResult.getName());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals("testName", objectResult.getSummary());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

    @Test
    public void testSetAndGetExportPaths() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://test.com/export1"));
        exportPaths.add(new URL("http://test.com/export2"));
        objectResult.setExportPaths(exportPaths);
        List<URL> actualExportPaths = objectResult.getExportPaths();
        assertEquals(2, actualExportPaths.size());
        assertEquals("http://test.com/export1", actualExportPaths.get(0).toString());
        assertEquals("http://test.com/export2", actualExportPaths.get(1).toString());
    }
}
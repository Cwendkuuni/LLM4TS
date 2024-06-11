package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(10), new AtomicLong(20), "testType");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("testName", objectResult.getName());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
        assertEquals("testType", objectResult.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        String expectedSummary = "testSchema.testName[testType]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getExportPaths());
        objectResult.setExportPaths(null);
        assertNull(objectResult.getExportPaths());
    }
}
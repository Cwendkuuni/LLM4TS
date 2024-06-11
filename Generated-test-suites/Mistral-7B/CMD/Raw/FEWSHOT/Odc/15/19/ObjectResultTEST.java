package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstruction() {
        ObjectResult objResult = new ObjectResult("testSchema", "testName", "testType");

        assertEquals("testSchema", objResult.getSchema());
        assertEquals("testName", objResult.getName());
        assertEquals("testType", objResult.getType());
        assertNull(objResult.getExportPaths());
        assertEquals(ObjectStatus.STATUS_SUCCESS, objResult.getStatus());
        assertEquals(0, objResult.getCount().get());
        assertEquals(0, objResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objStatus = new ObjectStatus("testType", ObjectStatus.STATUS_SUCCESS, "testName", "testSchema", new AtomicLong(10), new AtomicLong(15));
        ObjectResult objResult = ObjectResult.of(objStatus);

        assertEquals("testType", objResult.getType());
        assertEquals(ObjectStatus.STATUS_SUCCESS, objResult.getStatus());
        assertEquals("testName", objResult.getName());
        assertEquals("testSchema", objResult.getSchema());
        assertEquals(new AtomicLong(10), objResult.getCount());
        assertEquals(new AtomicLong(15), objResult.getTotal());
        assertNull(objResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objResult = new ObjectResult("testSchema", "testName", "testType");
        String summary = objResult.getSummary();

        assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objResult = new ObjectResult("testSchema", "testName", "testType");

        List<URL> exportPaths = new ArrayList<>();
        URL url = new URL("testUrl");
        exportPaths.add(url);

        objResult.setExportPaths(exportPaths);

        assertEquals(exportPaths, objResult.getExportPaths());
        assertEquals(url, objResult.getExportPaths().get(0));
    }
}
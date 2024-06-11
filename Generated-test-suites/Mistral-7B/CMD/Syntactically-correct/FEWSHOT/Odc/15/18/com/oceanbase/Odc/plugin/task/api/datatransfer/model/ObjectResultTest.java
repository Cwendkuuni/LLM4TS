package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(5), new AtomicLong(10), null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertSame(objectStatus.getStatus(), objectResult.getStatus());
        assertSame(objectStatus.getName(), objectResult.getName());
        assertSame(objectStatus.getType(), objectResult.getType());
        assertSame(objectStatus.getCount(), objectResult.getCount());
        assertSame(objectStatus.getTotal(), objectResult.getTotal());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("testSchema");
        objectResult.setName("testName");
        objectResult.setType("testType");

        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult();

        List<URL> exportPaths = new ArrayList<>();
        URL testPath = new URL("test://testPath");
        exportPaths.add(testPath);

        objectResult.setExportPaths(exportPaths);

        assertSame(exportPaths, objectResult.getExportPaths());
        assertEquals(1, objectResult.getExportPaths().size());
        assertSame(testPath, objectResult.getExportPaths().get(0));
    }

    @Test
    public void testStripLeadingHyphensInGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("--testSchema");
        objectResult.setName("--testName");
        objectResult.setType("--testType");

        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

}
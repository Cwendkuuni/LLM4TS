package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("testSchema", "testName", "testType");
    }

    @Test
    public void testObjectResultConstructor() {
        Assert.assertEquals("testSchema", objectResult.getSchema());
        Assert.assertEquals("testName", objectResult.getName());
        Assert.assertEquals("testType", objectResult.getType());
        Assert.assertEquals(0, objectResult.getCount().get());
        Assert.assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(1), new AtomicLong(2), "testType");
        ObjectResult result = ObjectResult.of(objectStatus);
        Assert.assertEquals("testName", result.getName());
        Assert.assertEquals("testType", result.getType());
        Assert.assertEquals(1, result.getCount().get());
        Assert.assertEquals(2, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        Assert.assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://example.com"));
        objectResult.setExportPaths(exportPaths);
        Assert.assertEquals(exportPaths, objectResult.getExportPaths());
    }

}
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("test-schema", "test-name", "test-type");
        assertEquals("test-schema", objectResult.getSchema());
        assertEquals("test-name", objectResult.getName());
        assertEquals("test-type", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testObjectResultStaticFactoryMethod() {
        ObjectStatus objectStatus = new ObjectStatus("test-name", new AtomicLong(10), new AtomicLong(20), "test-type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("test-type", objectResult.getType());
        assertEquals("test-name", objectResult.getName());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("test-schema", "test-name", "test-type");
        String summary = objectResult.getSummary();
        assertEquals("test-schema.test-name[test-type]", summary);
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://example.com"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
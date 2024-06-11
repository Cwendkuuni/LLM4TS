
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResult() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(new AtomicLong(0).longValue(), objectResult.getCount().get());
        assertEquals(new AtomicLong(0).longValue(), objectResult.getTotal().get());
        assertNull(objectResult.getStatus());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), "status");
        objectStatus.setSchema("schema");
        objectStatus.setType("type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals("status", objectResult.getStatus());
        assertEquals(new AtomicLong(10).longValue(), objectResult.getCount().get());
        assertEquals(new AtomicLong(20).longValue(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> urls = new ArrayList<>();
        objectResult.setExportPaths(urls);
        assertEquals(urls, objectResult.getExportPaths());
    }
}

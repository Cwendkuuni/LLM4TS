package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
        assertNull(objectResult.getStatus());
    }

    @Test
    public void testOf() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";
        String status = "test_status";
        AtomicLong count = new AtomicLong(123);
        AtomicLong total = new AtomicLong(456);

        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType(type);
        objectStatus.setStatus(status);
        objectStatus.setName(name);
        objectStatus.setSchema(schema);
        objectStatus.setCount(count);
        objectStatus.setTotal(total);

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertEquals(count.get(), objectResult.getCount().get());
        assertEquals(total.get(), objectResult.getTotal().get());
        assertEquals(status, objectResult.getStatus());
    }

    @Test
    public void testGetSummary() {
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        assertEquals(schema + "." + name + "[" + type + "]", objectResult.getSummary());
    }

    @Test
    public void testGetAndSetExportPaths() {
        ArrayList<URL> urls = new ArrayList<>();
        try {
            urls.add(new URL("http://test.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(urls);

        assertEquals(urls, objectResult.getExportPaths());
    }
}
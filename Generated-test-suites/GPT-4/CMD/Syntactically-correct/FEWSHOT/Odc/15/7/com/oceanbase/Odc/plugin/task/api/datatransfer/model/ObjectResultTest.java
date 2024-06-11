package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schema1", "name1", "type1");
    }

    @Test
    public void testConstructor() {
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(0L, objectResult.getCount().get());
        assertEquals(0L, objectResult.getTotal().get());
        assertNull(objectResult.getStatus());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema("schema2");
        objectStatus.setName("name2");
        objectStatus.setType("type2");
        objectStatus.setCount(new AtomicLong(2));
        objectStatus.setTotal(new AtomicLong(5));
        objectStatus.setStatus("Ongoing");
        ObjectResult objectResult2 = ObjectResult.of(objectStatus);
        assertEquals("schema2", objectResult2.getSchema());
        assertEquals("name2", objectResult2.getName());
        assertEquals("type2", objectResult2.getType());
        assertEquals(2L, objectResult2.getCount().get());
        assertEquals(5L, objectResult2.getTotal().get());
        assertEquals("Ongoing", objectResult2.getStatus());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("schema1.name1[type1]", summary);
    }

    @Test
    public void testSetAndGetExportPaths() {
        List<URL> urls = new ArrayList<>();
        objectResult.setExportPaths(urls);
        assertEquals(urls, objectResult.getExportPaths());
    }
}
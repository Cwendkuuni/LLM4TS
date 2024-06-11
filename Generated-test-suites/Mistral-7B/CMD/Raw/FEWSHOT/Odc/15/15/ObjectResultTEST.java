package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCEEDED, objectResult.getStatus());
        assertEquals(0L, objectResult.getCount().get());
        assertEquals(0L, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus that = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult result = ObjectResult.of(that);
        assertSame(that, result);
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    public void testSetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1"), new URL("http://localhost:8080/export2"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testGetExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export1"), new URL("http://localhost:8080/export2"));
        objectResult.setExportPaths(exportPaths);
        assertEquals(exportPaths, objectResult.getExportPaths());
        List<URL> emptyExportPaths = Collections.emptyList();
        assertNull(ObjectResult.of(new ObjectStatus("name1")).getExportPaths());
        assertNull(ObjectResult.of(new ObjectStatus("name1", new AtomicLong(1), null, null)).getExportPaths());
    }
}
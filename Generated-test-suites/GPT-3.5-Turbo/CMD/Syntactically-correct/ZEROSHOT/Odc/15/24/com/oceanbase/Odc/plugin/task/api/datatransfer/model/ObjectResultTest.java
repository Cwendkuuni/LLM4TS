package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertNotNull(objectResult);
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", null, null, null);
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String summary = objectResult.getSummary();
        assertEquals("schema.name[type]", summary);
    }

    @Test
    public void testExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://example.com/1"));
        exportPaths.add(new URL("http://example.com/2"));
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(exportPaths);
        assertEquals(2, objectResult.getExportPaths().size());
    }
}
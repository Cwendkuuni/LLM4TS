package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicLong;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ObjectResultTest {

    @Test
    public void testObjectResultCreation() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), "testStatus");
        status.setSchema("schema");
        status.setType("type");
        ObjectResult result = ObjectResult.of(status);
        assertNotNull(result);
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertEquals(status.getCount().get(), result.getCount().get());
        assertEquals(status.getTotal().get(), result.getTotal().get());
        assertEquals("testStatus", result.getStatus());
    }

    @Test
    public void testObjectResultExportPaths() {
        ObjectResult result = new ObjectResult();
        List<URL> paths = new ArrayList<>();
        result.setExportPaths(paths);
        assertEquals(paths, result.getExportPaths());
    }

    @Test
    public void testObjectResultGetSummary() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        String summary = result.getSummary();
        assertEquals("schema.name[type]", summary);
    }
}
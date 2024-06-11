package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        
        assertNotNull(objectResult);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
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
        
        assertNotNull(summary);
        assertEquals("schema.name[type]", summary);
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://example.com"));
        
        objectResult.setExportPaths(exportPaths);

        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}
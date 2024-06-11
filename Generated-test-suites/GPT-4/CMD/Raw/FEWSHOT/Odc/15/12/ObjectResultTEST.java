
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {
    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
    }
    
    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(1), new AtomicLong(1), "Completed");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
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
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getExportPaths());
        
        List<URL> paths = new ArrayList<>();
        objectResult.setExportPaths(paths);
        assertNotNull(objectResult.getExportPaths());
    }
}


package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
        assertNull(result.getStatus());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(5), new AtomicLong(10), ObjectStatus.Status.NEW);
        objectStatus.setType("type");
        objectStatus.setSchema("schema");
        ObjectResult result = ObjectResult.of(objectStatus);

        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getType(), result.getType());
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
        assertEquals(objectStatus.getStatus(), result.getStatus());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", result.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult result = new ObjectResult();
        List<URL> paths = Collections.emptyList();
        result.setExportPaths(paths);
        assertEquals(paths, result.getExportPaths());
    }
}

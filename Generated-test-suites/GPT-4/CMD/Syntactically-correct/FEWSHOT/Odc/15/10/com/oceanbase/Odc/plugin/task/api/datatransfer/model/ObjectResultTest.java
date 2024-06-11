
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.oceanbase.tools.loaddump.common.model.ObjectStatus;


public class ObjectResultTest {
    @Test
    public void testObjectResultConstructor() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus status = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(2), null);
        status.setType("type1");
        status.setSchema("schema1");

        ObjectResult result = ObjectResult.of(status);
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(1, result.getCount().get());
        assertEquals(2, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", result.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult result = new ObjectResult();
        assertNull(result.getExportPaths());

        List<URL> paths = Collections.singletonList(getClass().getResource("/"));
        result.setExportPaths(paths);
        assertNotNull(result.getExportPaths());
        assertEquals(paths, result.getExportPaths());
    }
}

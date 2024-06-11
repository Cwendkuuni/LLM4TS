
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {
    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schema", "name", "type");
        objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
    }

    @Test
    public void testOf() {
        ObjectResult result = ObjectResult.of(objectStatus);

        assertNotNull(result);
        assertEquals("name", result.getName());
        assertEquals("schema", result.getSchema());
        assertEquals("type", result.getType());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();

        assertNotNull(summary);
        assertEquals("schema.name[type]", summary);
    }

    @Test
    public void testGetAndSetExportPaths() {
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);

        assertEquals(exportPaths, objectResult.getExportPaths());
    }
}


package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ObjectResultTest {
    private ObjectResult objectResult;
    private ObjectStatus objectStatus;
    private String schema = "testSchema";
    private String name = "testName";
    private String type = "testType";

    @Before
    public void setUp() {
        objectStatus = new ObjectStatus(name, new AtomicLong(0), new AtomicLong(0), null);
        objectResult = new ObjectResult(schema, name, type);
    }

    @Test
    public void testOf() {
        ObjectResult res = ObjectResult.of(objectStatus);
        assertEquals(name, res.getName());
        assertEquals(schema, res.getSchema());
        assertEquals(type, res.getType());
    }

    @Test
    public void testGetSetExportPaths() {
        List<URL> paths = new ArrayList<>();
        objectResult.setExportPaths(paths);
        assertEquals(paths, objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertTrue(summary.contains(schema));
        assertTrue(summary.contains(name));
        assertTrue(summary.contains(type));
    }

    @Test
    public void testEqualsAndHashCode() {
        ObjectResult otherObjectResult = new ObjectResult(schema, name, type);
        assertEquals(objectResult, otherObjectResult);
        assertEquals(objectResult.hashCode(), otherObjectResult.hashCode());
    }
}

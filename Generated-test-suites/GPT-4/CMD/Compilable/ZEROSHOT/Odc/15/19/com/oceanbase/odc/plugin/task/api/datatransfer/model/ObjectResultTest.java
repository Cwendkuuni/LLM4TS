package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class ObjectResultTest {
    private ObjectStatus objectStatus;
    private ObjectResult objectResult;
    private final String schema = "testSchema";
    private final String name = "testName";
    private final String type = "testType";

    @Before
    public void setUp() throws Exception {
        objectStatus = new ObjectStatus(name, new AtomicLong(0), new AtomicLong(0), null);
        objectResult = new ObjectResult(schema, name, type);
    }

    @Test
    public void testConstructor() {
        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testOf() {
        ObjectResult result = ObjectResult.of(objectStatus);
        assertNotNull(result);
        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getType(), result.getType());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertNotNull(summary);
        assertEquals(String.format("%s.%s[%s]", schema, name, type), summary);
    }

    @Test
    public void testExportPaths() {
        List<URL> paths = Collections.emptyList();
        objectResult.setExportPaths(paths);
        assertEquals(paths, objectResult.getExportPaths());
    }
}

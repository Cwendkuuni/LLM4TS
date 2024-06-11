package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import java.util.List;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private final String schema = "testSchema";
    private final String name = "testName";
    private final String type = "testType";

    @Before
    public void setUp() throws Exception {
        objectResult = new ObjectResult(schema, name, type);

        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://localhost:8000/fakepath"));
        objectResult.setExportPaths(exportPaths);
    }

    @Test
    public void testSetType() {
        objectResult.setType("newType");
        assertEquals("newType", objectResult.getType());
    }

    @Test
    public void testSetSchema() {
        objectResult.setSchema("newSchema");
        assertEquals("newSchema", objectResult.getSchema());
    }

    @Test
    public void testSetName() {
        objectResult.setName("newName");
        assertEquals("newName", objectResult.getName());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType(type);
        objectStatus.setName(name);
        objectStatus.setSchema(schema);
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(100));

        ObjectResult result = ObjectResult.of(objectStatus);

        assertEquals(name, result.getName());
        assertEquals(schema, result.getSchema());
        assertEquals(type, result.getType());
        assertEquals(10, result.getCount().get());
        assertEquals(100, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        String expected = schema + "." + name + "[" + type + "]"; 
        assertEquals(expected, summary);
    }

    @Test
    public void testGetExportPaths() {
        List<URL> exportPaths = objectResult.getExportPaths();
        assertNotNull(exportPaths);
        assertEquals(1, exportPaths.size());
    }
}

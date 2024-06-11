
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.Before;

public class ObjectResultTest {

    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectStatus = new ObjectStatus("testName", new AtomicLong(10), new AtomicLong(10), "completed");
        objectStatus.setType("myType");
        objectStatus.setSchema("mySchema");
    }

    @Test
    public void testObjectResult() {
        ObjectResult result = new ObjectResult("myschema", "myname", "mytype");
        assertEquals("myschema", result.getSchema());
        assertEquals("myname", result.getName());
        assertEquals("mytype", result.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getType(), result.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("myschema", "myname", "mytype");
        String expected = "myschema.myname[mytype]";
        assertEquals(expected, result.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult result = new ObjectResult();
        List<URL> testList = new ArrayList<>();
        assertNull(result.getExportPaths());
        result.setExportPaths(testList);
        assertEquals(testList, result.getExportPaths());
    }
}

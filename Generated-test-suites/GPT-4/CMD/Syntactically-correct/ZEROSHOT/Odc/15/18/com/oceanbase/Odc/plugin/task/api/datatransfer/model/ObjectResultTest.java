package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectResultTest {
    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("testSchema", "testName", "testType");
        objectResult.setExportPaths(Arrays.asList());
    }

    @Test
    public void testConstructor() {
        assertNotNull(new ObjectResult());
        assertNotNull(new ObjectResult("mySchema", "myName", "myType"));
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("objectName", new AtomicLong(0), new AtomicLong(0), "status");
        objectStatus.setType("objectType");
        objectStatus.setSchema("objectSchema");

        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals("objectName", result.getName());
        assertEquals("objectStatus", result.getStatus());
        assertEquals("objectType", result.getType());
        assertEquals("objectSchema", result.getSchema());
        assertEquals(0L, result.getCount().get());
        assertEquals(0L, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testSetAndViewExportPaths() {
        URL url = new URL("http://example.com");
        objectResult.setExportPaths(Arrays.asList(url));
        assertEquals(url, objectResult.getExportPaths().get(0));
    }
}

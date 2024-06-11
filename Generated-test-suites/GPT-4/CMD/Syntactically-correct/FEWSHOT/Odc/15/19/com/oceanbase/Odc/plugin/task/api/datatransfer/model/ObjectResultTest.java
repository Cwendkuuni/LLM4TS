package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schemaTest", "nameTest", "typeTest");
    }

    @Test
    public void testObjectSummary() {
        assertEquals("schemaTest.nameTest[typeTest]", objectResult.getSummary());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus testStatus = new ObjectStatus("test", new AtomicLong(5), new AtomicLong(10), "Processing");
        ObjectResult newObjectResult = ObjectResult.of(testStatus);
        
        assertEquals("test", newObjectResult.getName());
        assertEquals("Processing", newObjectResult.getStatus());
        assertEquals(5, newObjectResult.getCount().get());
        assertEquals(10, newObjectResult.getTotal().get());
    }

    @Test
    public void testGetExportPaths() {
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testSetExportPaths() {
        objectResult.setExportPaths(Arrays.asList());
        assertEquals(0, objectResult.getExportPaths().size());
    }
}
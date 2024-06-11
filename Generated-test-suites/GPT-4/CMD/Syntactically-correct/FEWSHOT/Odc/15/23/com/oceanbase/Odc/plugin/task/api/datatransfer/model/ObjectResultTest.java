package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    private ObjectStatus objectStatus;
    private ObjectResult objectResult;

    @Before
    public void setUp(){
        objectStatus = new ObjectStatus("Test name", new AtomicLong(20), new AtomicLong(30), ObjectStatus.Status.PENDING);
        objectResult = new ObjectResult("Test schema", "Test name", "Test type");
    }

    @Test
    public void testObjectResultConstructor() {
        assertNotNull(objectResult);
        assertEquals("Test schema", objectResult.getSchema());
        assertEquals("Test name", objectResult.getName());
        assertEquals("Test type", objectResult.getType());
    }

    @Test
    public void testOf() {
        ObjectResult resultFromStatus = ObjectResult.of(objectStatus);
        assertNotNull(resultFromStatus);
        assertEquals(objectStatus.getSchema(), resultFromStatus.getSchema());
        assertEquals(objectStatus.getName(), resultFromStatus.getName());
        assertEquals(objectStatus.getType(), resultFromStatus.getType());
        assertEquals(objectStatus.getCount().get(), resultFromStatus.getCount().get());
        assertEquals(objectStatus.getTotal().get(), resultFromStatus.getTotal().get());
    }
    
    @Test
    public void testGetSummary() {
        String expectedSummary = "Test schema.Test name[Test type]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testListExportPaths(){
        assertNull(objectResult.getExportPaths());
        ArrayList<URL> urlList = new ArrayList<>();
        objectResult.setExportPaths(urlList);
        assertNotNull(objectResult.getExportPaths());
    }
}
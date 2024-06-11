package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class ObjectResultTest {

    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(1), "InProgress");
        objectResult = new ObjectResult("schema1", "name1", "type1");
    }

    @After
    public void tearDown() {
        objectResult = null;
        objectStatus = null;
    }

    @Test
    public void testObjectResult() {
        Assert.assertEquals("schema1", objectResult.getSchema());
        Assert.assertEquals("name1", objectResult.getName());
        Assert.assertEquals("type1", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectResult newResult = ObjectResult.of(objectStatus);
        Assert.assertEquals("name1", newResult.getName());
        Assert.assertEquals("InProgress", newResult.getStatus());
        Assert.assertEquals(1, newResult.getCount().get());
        Assert.assertEquals(1, newResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        Assert.assertEquals("schema1.name1[type1]", summary);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        ObjectResult result = ObjectResult.of(null);
    }
    
    @Test
    public void testGetExportPaths() {
        List<URL> lists = new ArrayList<>();
        objectResult.setExportPaths(lists);
        Assert.assertEquals(lists, objectResult.getExportPaths());
    }

}


package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(10), new AtomicLong(20), "testType");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        String summary = objectResult.getSummary();
        
        assertEquals("testSchema.testName[testType]", summary);
    }

}
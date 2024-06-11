
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setup() {
        objectResult = new ObjectResult("testSchema", "testName", "testType");
        objectStatus = new ObjectStatus("testName",new AtomicLong(0),new AtomicLong(0),"testType");
    }
    
    @Test
    public void constructorTest() {
        assertNotNull(objectResult);
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
    }

    @Test
    public void ofTest() {
        ObjectResult newResult = ObjectResult.of(objectStatus);
        
        assertEquals("testName", newResult.getName());
        assertEquals("testType", newResult.getType());
        assertTrue(newResult.getStatus() == null);
        assertEquals(0, newResult.getCount().get());
        assertEquals(0, newResult.getTotal().get());
    }

    @Test
    public void getSummaryTest() {
        String expected = "testSchema.testName[testType]";
        assertEquals(expected, objectResult.getSummary());
    }
}

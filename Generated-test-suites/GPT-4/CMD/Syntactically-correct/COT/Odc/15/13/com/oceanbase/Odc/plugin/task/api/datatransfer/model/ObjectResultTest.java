package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("name2", new AtomicLong(10), new AtomicLong(20), "STATUS_COMPLETE");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema3", "name3", "type3");
        String expectedSummary = "schema3.name3[type3]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    // Test cases for edge cases and exceptions
  
    @Test(expected = NullPointerException.class)
    public void testNullObjectStatus() {
        ObjectResult.of(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullProperties() {
        new ObjectResult(null, null, null);
    }

    @Test()
    public void testEmptyProperties() {
        ObjectResult objectResult = new ObjectResult("", "", "");
        assertEquals(".", objectResult.getSummary());
    }
}
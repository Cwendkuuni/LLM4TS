package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    private ObjectResult objectResult1;
    private ObjectResult objectResult2;

    @Before
    public void setUp() throws Exception {
        objectResult1 = new ObjectResult("schema1", "name1", "type1");
        objectResult2 = ObjectResult.of(objectResult1);
    }

    @Test
    // Test case for constructor method
    public void testCreateObjectResult() throws Exception {
        assertNotNull(objectResult1);
        assertEquals("schema1", objectResult1.getSchema());
        assertEquals("name1", objectResult1.getName());
        assertEquals("type1", objectResult1.getType());
        assertEquals(0, objectResult1.getCount().get());
        assertEquals(0, objectResult1.getTotal().get());
    }

    @Test
    // Test case for of method
    public void testOf() throws Exception {
        assertNotNull(objectResult2);
        assertEquals("schema1", objectResult2.getSchema());
        assertEquals("name1", objectResult2.getName());
        assertEquals("type1", objectResult2.getType());
        assertEquals(0, objectResult2.getCount().get());
        assertEquals(0, objectResult2.getTotal().get());
    }

    @Test
    // Test case for getSummary method
    public void testGetSummary() throws Exception {
        String expected = "schema1.name1[type1]";
        assertEquals(expected, objectResult1.getSummary());
        assertEquals(expected, objectResult2.getSummary());
    }

    @Test
    // Test case for atomic long 
    public void testAtomicLong() throws Exception {
        objectResult1.getCount().set(1);
        objectResult1.getTotal().set(2);
        assertEquals(1, objectResult1.getCount().get());
        assertEquals(2, objectResult1.getTotal().get());
    }

    @Test(expected = NullPointerException.class)
    // Test case for null pointer on of method
    public void nullTestOf() {
        ObjectResult.of(null);
    }
}
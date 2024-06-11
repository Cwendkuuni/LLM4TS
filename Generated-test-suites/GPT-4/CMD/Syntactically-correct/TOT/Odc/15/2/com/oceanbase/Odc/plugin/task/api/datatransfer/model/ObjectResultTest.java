package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    private ObjectResult result;

    @Before
    public void setUp() {
        result = new ObjectResult("testSchema", "testName", "testType");
    }

    @Test
    public void testOf() {
        ObjectResult testResult = ObjectResult.of(result);
        assertEquals(result.getCount().get(), testResult.getCount().get());
        assertEquals(result.getTotal().get(), testResult.getTotal().get());
        assertEquals(result.getSchema(), testResult.getSchema());
        assertEquals(result.getName(), testResult.getName());
        assertEquals(result.getType(), testResult.getType());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = "testSchema.testName[testType]";
        assertEquals(expectedSummary, result.getSummary());
    }

    // You can add more test cases here, covering edge cases and invalid inputs
}
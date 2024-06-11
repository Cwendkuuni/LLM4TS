package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.*;

public class ObjectResultTest {
    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus original = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), ObjectStatus.StatusType.Completed);
        original.setSchema("schema");
        original.setType("type");

        ObjectResult result = ObjectResult.of(original);

        assertEquals(original.getSchema(), result.getSchema());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getType(), result.getType());
        assertEquals(original.getCount().get(), result.getCount().get());
        assertEquals(original.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testGetSummaryMethod() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String expectedSummary = "schema.name[type]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    // Additional test cases for edge cases and error scenarios
    // ...
}
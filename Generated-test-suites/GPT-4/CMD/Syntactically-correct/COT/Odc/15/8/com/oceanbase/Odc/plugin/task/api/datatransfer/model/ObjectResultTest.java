package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        status.setSchema("schema");
        status.setType("type");
        ObjectResult result = ObjectResult.of(status);
        assertEquals(status.getSchema(), result.getSchema());
        assertEquals(status.getName(), result.getName());
        assertEquals(status.getType(), result.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", result.getSummary());
    }

    @Test
    public void testNullInputsConstructor() {
        ObjectResult result = new ObjectResult(null, null, null);
        assertEquals(null, result.getSchema());
        assertEquals(null, result.getName());
        assertEquals(null, result.getType());
    }

    @Test
    public void testNullInputsOf() {
        ObjectStatus status = new ObjectStatus(null, new AtomicLong(0), new AtomicLong(0), null);
        status.setType(null);
        ObjectResult result = ObjectResult.of(status);
        assertEquals(status.getSchema(), result.getSchema());
        assertEquals(status.getName(), result.getName());
        assertEquals(status.getType(), result.getType());
    }

    @Test
    public void testGetSummaryWithNullFields() {
        ObjectResult result = new ObjectResult(null, null, null);
        assertEquals("null.null[null]", result.getSummary());
    }
}
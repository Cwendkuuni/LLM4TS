package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    
    @Test
    public void testConstructor() {
        ObjectResult obj = new ObjectResult("schema", "name", "type");
        Assert.assertEquals("Name check", "name", obj.getName());
        Assert.assertEquals("Schema check", "schema", obj.getSchema());
        Assert.assertEquals("Type check", "type", obj.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        status.setType("type");
        status.setSchema("schema");
        ObjectResult obj = ObjectResult.of(status);
        Assert.assertEquals("Name check", "name", obj.getName());
        Assert.assertEquals("Schema check", "schema", obj.getSchema());
        Assert.assertEquals("Type check", "type", obj.getType());
    }

    @Test
    public void testGetSummary() {
        ObjectResult obj = new ObjectResult("schema", "name", "type");
        String expectedSummary = "schema.name[type]";
        String actualSummary = obj.getSummary();
        Assert.assertEquals("Summary check", expectedSummary, actualSummary);
    }
}
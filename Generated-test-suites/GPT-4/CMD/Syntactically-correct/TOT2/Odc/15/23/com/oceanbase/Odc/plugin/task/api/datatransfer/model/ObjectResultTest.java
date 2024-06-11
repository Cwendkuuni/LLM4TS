package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import org.junit.Test;
import static org.junit.Assert.*;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    @Test
    public void testConstructor() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");

        assertNotNull(result);
        assertEquals("schema1", result.getSchema());
        assertEquals("name1", result.getName());
        assertEquals("type1", result.getType());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("name2", new AtomicLong(1), new AtomicLong(2), "start");
        ObjectResult result = ObjectResult.of(status);

        assertNotNull(result);
        assertEquals(status.getName(), result.getName());
        assertEquals(status.getSchema(), result.getSchema());
        assertEquals(status.getCount().get(), result.getCount().get());
        assertEquals(status.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        String expectedSummary = "schema1.name1[type1]";

        assertEquals(expectedSummary, result.getSummary());
    }
}
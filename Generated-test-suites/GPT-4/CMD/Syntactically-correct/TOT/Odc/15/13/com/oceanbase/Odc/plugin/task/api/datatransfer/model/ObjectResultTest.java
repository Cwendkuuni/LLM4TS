package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus(
                "name1", new AtomicLong(2), new AtomicLong(3), "status1");
        objectStatus.setType("type1");
        objectStatus.setSchema("schema1");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        String summary = objectResult.getSummary();
        assertEquals("schema1.name1[type1]", summary);
    }
}
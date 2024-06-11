
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicLong;
import org.junit.Test;

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
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(100));
        objectStatus.setType("type");
        objectStatus.setSchema("schema");

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        String expectedSummary = "schema.name[type]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }
}

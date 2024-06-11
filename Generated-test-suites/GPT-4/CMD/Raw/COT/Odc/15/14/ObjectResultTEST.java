package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertNotNull(objectResult);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("type");
        objectStatus.setStatus("status");
        objectStatus.setName("name");
        objectStatus.setSchema("schema");
        objectStatus.setCount(new AtomicLong(5));
        objectStatus.setTotal(new AtomicLong(10));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertNotNull(objectResult);
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }
    
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");

        String summary = objectResult.getSummary();
        
        assertEquals("schema.name[type]", summary);
    }
}
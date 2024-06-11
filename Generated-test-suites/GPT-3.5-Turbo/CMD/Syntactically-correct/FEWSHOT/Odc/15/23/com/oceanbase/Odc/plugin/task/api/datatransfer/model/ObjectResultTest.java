package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("name2", new AtomicLong(10), new AtomicLong(20), "type2");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals("name2", objectResult.getName());
        assertEquals("type2", objectResult.getType());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema3", "name3", "type3");
        String summary = objectResult.getSummary();
        assertNotNull(summary);
        assertEquals("schema3.name3[type3]", summary);
    }
}
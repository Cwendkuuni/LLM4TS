package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
        assertNull(objectResult.getStatus());
    }

    @Test
    public void testOfFunction() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema("schemaOS");
        objectStatus.setName("nameOS");
        objectStatus.setType("typeOS");
        objectStatus.setCount(new AtomicLong(1000));
        objectStatus.setTotal(new AtomicLong(2000));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("schemaOS", objectResult.getSchema());
        assertEquals("nameOS", objectResult.getName());
        assertEquals("typeOS", objectResult.getType());
        assertEquals(1000, objectResult.getCount().get());
        assertEquals(2000, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertTrue(objectResult.getSummary().contains(objectResult.getSchema()));
        assertTrue(objectResult.getSummary().contains(objectResult.getName()));
        assertTrue(objectResult.getSummary().contains(objectResult.getType()));
    }
}
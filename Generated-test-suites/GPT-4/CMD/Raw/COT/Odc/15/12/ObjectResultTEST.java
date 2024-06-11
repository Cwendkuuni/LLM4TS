
package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertEquals(0, objectResult.getTotal().get());
        assertEquals(0, objectResult.getCount().get());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("name");
        objectStatus.setSchema("schema");
        objectStatus.setType("type");
        objectStatus.setTotal(new AtomicLong(5));
        objectStatus.setCount(new AtomicLong(3));
        objectResult = ObjectResult.of(objectStatus);
        
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals(5, objectResult.getTotal().get());
        assertEquals(3, objectResult.getCount().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        String expected = "schema1.name1[type1]";
        
        assertEquals(expected, objectResult.getSummary());
    }
    
    @Test(expected = NullPointerException.class)
    public void testOf_NullObjectStatus() {
        ObjectResult objectResult = ObjectResult.of(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetSummary_NullProperties() {
        ObjectResult objectResult = new ObjectResult();
        String summary = objectResult.getSummary();
    }
}

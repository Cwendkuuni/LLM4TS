package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import org.junit.Test;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.*;

public class ObjectResultTest {  
    @Test
    public void test_ObjectResult_constructor() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
    }
    
    @Test
    public void test_of() {
        ObjectStatus status = new ObjectStatus();
        status.setName("name");
        status.setSchema("schema");
        status.setType("type");
        status.setTotal(new AtomicLong(500));
        status.setCount(new AtomicLong(200));
        
        ObjectResult result = ObjectResult.of(status);
        
        assertEquals(status.getName(), result.getName());
        assertEquals(status.getSchema(), result.getSchema());
        assertEquals(status.getType(), result.getType());
        assertEquals(status.getTotal().get(), result.getTotal().get());
        assertEquals(status.getCount().get(), result.getCount().get());
    }
    
    @Test
    public void test_getSummary() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        String expectedSummary = "schema.name[type]";
        assertEquals(expectedSummary, result.getSummary());
    } 
    
    @Test(expected = NullPointerException.class)
    public void test_of_exception() {
        ObjectResult.of(null);
    }
}

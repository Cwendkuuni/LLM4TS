package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {
    private ObjectResult objectResult;
    
    @Before
    public void setUp() {
        objectResult = new ObjectResult();
    }

    @Test
    public void testConstructor() {
        String schema = "schema";
        String name = "name";
        String type = "type";
        ObjectResult result = new ObjectResult(schema, name, type);
        assertEquals(schema, result.getSchema());
        assertEquals(name, result.getName());
        assertEquals(type, result.getType());
    }

    @Test
    public void testOfObjectStatus() {
        ObjectStatus objStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), "status");
        ObjectResult result = ObjectResult.of(objStatus);
        
        assertEquals(objStatus.getSchema(), result.getSchema());
        assertEquals(objStatus.getName(), result.getName());
        assertEquals(objStatus.getType(), result.getType());
        assertEquals(objStatus.getStatus(), result.getStatus());
    }

    @Test
    public void testGetSummary() {
        String schema = "schema";
        String name = "name";
        String type = "type";
        objectResult.setSchema(schema);
        objectResult.setName(name);
        objectResult.setType(type);
        
        String expectedSummary = String.format("%s.%s[%s]", schema, name, type);
        String actualSummary = objectResult.getSummary();
        
        assertEquals(expectedSummary, actualSummary);
    }

    // Test edge cases and handle exceptions here.
}
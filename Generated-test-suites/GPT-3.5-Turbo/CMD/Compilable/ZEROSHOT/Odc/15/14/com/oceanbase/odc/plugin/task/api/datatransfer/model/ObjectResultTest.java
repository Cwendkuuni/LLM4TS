package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testOfMethod() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        ObjectResult result = ObjectResult.of(objectResult);
        
        assertEquals(objectResult.getType(), result.getType());
        assertEquals(objectResult.getStatus(), result.getStatus());
        assertEquals(objectResult.getName(), result.getName());
        assertEquals(objectResult.getSchema(), result.getSchema());
        assertEquals(objectResult.getCount().get(), result.getCount().get());
        assertEquals(objectResult.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testGetSummaryMethod() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        String summary = objectResult.getSummary();
        
        assertEquals("testSchema.testName[testType]", summary);
    }
}
package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ObjectResultTest {

    private static final String SCHEMA = "TestSchema";
    private static final String NAME = "TestName";
    private static final String TYPE = "TestType";

    @Test
    public void test_Constructor() {
        ObjectResult objectResult = new ObjectResult(SCHEMA, NAME, TYPE);
        
        assertEquals(SCHEMA, objectResult.getSchema());
        assertEquals(TYPE, objectResult.getType());
        assertEquals(NAME, objectResult.getName());
    }
   
    @Test
    public void test_Of() {
        ObjectStatus objectStatus = new ObjectStatus();
        // Set up
        objectStatus.setSchema(SCHEMA);
        objectStatus.setType(TYPE);
        objectStatus.setName(NAME);

        ObjectResult testResult = ObjectResult.of(objectStatus);
        
        assertEquals(testResult.getName(), objectStatus.getName());
        assertEquals(testResult.getSchema(), objectStatus.getSchema());
        assertEquals(testResult.getType(), objectStatus.getType());
    }

    @Test
    public void test_getSummary() {
        ObjectResult objectResult = new ObjectResult(SCHEMA, NAME, TYPE);
        String expectedSummary = SCHEMA + "." + NAME + "[" + TYPE + "]";

        String summary = objectResult.getSummary();
        
        assertEquals(expectedSummary, summary);
    }    
   
    // Now the edge cases
    // When null input are given to constructor
    @Test(expected = NullPointerException.class)
    public void test_Constructor_NullInputs() {
        new ObjectResult(null, null, null);
    }
   
    // when ObjectStatus parameter is null
    @Test(expected = NullPointerException.class)
    public void test_Of_NullParameter() {
        ObjectResult.of(null);
    }
    
    // when ObjectResult fields are null
    @Test
    public void test_getSummary_NullFields() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
        String expectedSummary = "null.null[null]";
        
        assertEquals(expectedSummary, objectResult.getSummary());
    }  
}
package com.netflix.conductor.core.utils;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParametersUtilsTest {
    
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
        // setup your mock data here
    }

    @Test
    public void testGetTaskInput() {
        //Prepare input values & expected results

        //Call the method
        Map<String, Object> result = parametersUtils.getTaskInput(/*fill with the necessary parameters*/);

        //Validate the result
        assertNotNull(result);
        //assertTrue or assertFalse or assertEquals, according to your needs
    }
    
    @Test
    public void testGetTaskInputV2() {
        // same as above for this method
    }

    @Test
    public void testClone() {
        // same as above for this method
    }

    @Test
    public void testReplace() {
        // same as above for this method
    }

    @Test
    public void testReplaceString() {
        // same as above for this method
    }

    @Test
    public void testReplaceVariables() {
        // same as above for this method
    }

    @Test
    public void testGetTaskInputV1() {
        // same as above for this method
    }

    @Test
    public void testGetWorkflowInput() {
        // same as above for this method
    }

    
    @After
    public void tearDown() {
        parametersUtils = null;
        // perform clean up here if necessary
    }
}
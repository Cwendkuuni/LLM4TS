package com.netflix.conductor.core.utils;

import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParametersUtilsTest {
    ParametersUtils parametersUtils;
    
    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        assertNull(parametersUtils.getTaskInput(null, null, null, null));
        // add further test cases. 
    }

    @Test
    public void testGetTaskInputV2() {
        assertNull(parametersUtils.getTaskInputV2(null, null, null, null));
        // add further test cases. 
    }

    @Test
    public void testReplace() {
        assertNull(parametersUtils.replace(null, null));
        // add further test cases. 
    }

    @Test
    public void testReplaceString() {
        assertEquals("", parametersUtils.replace(""));
        // add further test cases. 
    }

    @Test
    public void testGetWorkflowInput() {
        assertNull(parametersUtils.getWorkflowInput(null, null));
        // add further test cases. 
    }
}

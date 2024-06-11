package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.*;
import com.netflix.conductor.common.metadata.workflow.*;
import com.netflix.conductor.core.utils.*;
import com.netflix.conductor.model.*;

import com.fasterxml.jackson.databind.*;
import com.jayway.jsonpath.*;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;

/** Test to check ParametersUtils class in conductor */
public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    // Testing clone method
    @Test
    public void testClone() {
        Map<String, Object> original = new HashMap<>();
        original.put("key1", "value1");
        original.put("key2", "value2");
        Map<String, Object> duplicate = parametersUtils.clone(original);
        assertEquals(original, duplicate);
        duplicate.put("key3", "value3");
        assertNotEquals(original, duplicate);
    }

    // Testing replace method
    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "${val}");
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(new HashMap<String, Object>(){{put("val", "TEST");}}, option);
        Map<String, Object> modified = parametersUtils.replace(input, documentContext);
        assertEquals(input.get("key"), "TEST");
    }

    // Testing replace method with paramString
    @Test
    public void testReplaceWithParamString() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(new HashMap<String, Object>(){{put("val", "TEST");}}, option);
        String result = (String) parametersUtils.replace("${val}");
        assertEquals(result, "TEST");
    }

    // Testing replaceVariables method
    @Test
    public void testReplaceVariables() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(new HashMap<String, Object>(){{put("val", "TEST");}}, option);
        String result = (String) parametersUtils.replaceVariables("${val}", documentContext, null);
        assertEquals(result, "TEST");
    }

    // Not testing getTaskInput/getTaskInputV1/getTaskInputV2/getWorkflowInput as they require complex set up for WorkflowModel and others

    // Testing for IO Exception in clone
    @Test(expected = RuntimeException.class)
    public void testCloneForException() {
        ParametersUtils newParametersUtils = new ParametersUtils(new ObjectMapper(){
            private static final long serialVersionUID = 1L;
            @Override
            public byte[] writeValueAsBytes(Object value){
                throw new RuntimeException("IO Exception");
            }
        });
        newParametersUtils.clone(new HashMap<>());
    }
}


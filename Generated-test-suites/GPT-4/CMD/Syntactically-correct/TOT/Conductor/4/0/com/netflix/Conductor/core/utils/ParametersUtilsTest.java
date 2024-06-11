package com.netflix.conductor.core.utils;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class ParametersUtilsTest {

    private ParametersUtils utils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        utils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "value1");
        input.put("key2", "value2");

        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDefinition = mock(TaskDef.class);

        Map<String, Object> receivedInput = utils.getTaskInputV2(input, workflow,"task1", taskDefinition);

        assertEquals(input, receivedInput);
    }

    @Test
    public void testClone() {
        Map<String, Object> original = new HashMap<>();
        original.put("key1", "value1");
        original.put("key2", "value2");

        Map<String, Object> cloned = utils.clone(original);

        assertEquals(original, cloned);
        // Assert they are not the same object
        assert(original != cloned);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "value1");
        input.put("key2", "value2");

        Object doc = new HashMap<>();
        ((HashMap<String, Object>) doc).put("key1", "newValue1");
        ((HashMap<String, Object>) doc).put("key2", "newValue2");

        Map<String, Object> replacedInput = utils.replace(input, doc);

        assertEquals(doc, replacedInput);
    }

    @Test
    public void testReplaceString() {
        String param = "${test.param}";

        Object replacedString = utils.replace(param);

        assertEquals("", replacedString);
    }
}
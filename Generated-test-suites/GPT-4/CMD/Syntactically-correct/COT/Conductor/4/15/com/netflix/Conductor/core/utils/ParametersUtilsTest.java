package com.netflix.conductor.core.utils;
import org.junit.Before;
import org.junit.Test;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testReplace() {
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("test", "testValue");
        String jsonString = objectMapper.writeValueAsString(testMap);
        Map<String, Object> original = new HashMap<>();
        original.put("key", "value");
        Object actual = parametersUtils.replace(original, jsonString);
        // assert on the actual value
    }

    @Test
    public void testReplaceString() {
        String paramString = "testParamString";
        Object actual = parametersUtils.replace(paramString);
        // assert on the actual value
    }

    // Similarly write test methods for getTaskInput, getWorkflowInput and getTaskInputV2

}
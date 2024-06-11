package com.netflix.conductor.core.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.HashMap;

public class ParametersUtilsTest {

    @Test
    public void testReplace() {
        ParametersUtils paramsUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        input.put("name", "${value}");
        input.put("count", "10");
        String json = "{\"value\":\"test\"}";
        Map<String, Object> result = paramsUtils.replace(input, json);
        assertEquals("test", result.get("name"));
        assertEquals("10", result.get("count"));
    }

    @Test
    public void testReplaceVariables() {
        ParametersUtils paramsUtils = new ParametersUtils(new ObjectMapper());
        String replaced = paramsUtils.replace("${value}").toString();
        assertTrue(replaced.isEmpty());
    }

    @Test
    public void testClone() {
        ParametersUtils paramsUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        input.put("name", "test");
        input.put("count", 10);
        Map<String, Object> clone = paramsUtils.clone(input);
        assertEquals(input, clone);
    }

    @Test(expected = RuntimeException.class)
    public void testCloneWithException() {
        ObjectMapper objectMapper = org.mockito.Mockito.mock(ObjectMapper.class);
        org.mockito.Mockito.when(objectMapper.writeValueAsBytes(org.mockito.Mockito.any())).thenThrow(new IOException());
        ParametersUtils paramsUtils = new ParametersUtils(objectMapper);
        paramsUtils.clone(new HashMap<>());
    }
}
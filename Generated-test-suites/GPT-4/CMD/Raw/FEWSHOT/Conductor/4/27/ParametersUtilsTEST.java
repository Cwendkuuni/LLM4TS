package com.netflix.conductor.core.utils;

import org.junit.Test;

import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {
    ParametersUtils parametersUtils = new ParametersUtils();

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("foo", "${bar}");
        Map<String, Object> json = new HashMap<>();
        json.put("bar", "baz");

        Map<String, Object> actual = parametersUtils.replace(input, json);

        Assert.assertNotNull(actual);
        Assert.assertEquals("baz", actual.get("foo"));
    }

    @Test
    public void testReplaceWithStringInput() {
        Map<String, Object> input = new HashMap<>();
        input.put("foo", "${bar}");
        String jsonString = "{\"bar\":\"baz\"}";

        Map<String, Object> actual = parametersUtils.replace(input, jsonString);

        Assert.assertNotNull(actual);
        Assert.assertEquals("baz", actual.get("foo"));
    }

    @Test
    public void testReplaceWithParamString() {
        String actual = parametersUtils.replace("${bar}");

        Assert.assertNotNull(actual);
        Assert.assertEquals("${bar}", actual); // since no real replacement is performed in test
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("foo", "bar");

        Map<String, Object> actual = parametersUtils.getWorkflowInput(null, inputParams);

        Assert.assertNotNull(actual);
        Assert.assertEquals("bar", actual.get("foo"));
    }

    // The other methods to test would need either a real or mock object mapper and/or creating instances of specific classes (like WorkflowDef or WorkflowModel)
    // so they are not implemented here. In a real testing environment, Mockito or other testing frameworks should be used to provide these dependencies.
}
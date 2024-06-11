
package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {
    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;

    @Before
    public void setUp() throws Exception {
        objectMapper = Mockito.mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testReplace() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        Mockito.when(objectMapper.writeValueAsBytes(map)).thenReturn(new byte[1]);
        Mockito.when(objectMapper.readValue(new byte[1], Map.class)).thenReturn(map);
        Object result = parametersUtils.replace("${key}", map);
        assertEquals("value", result);
    }

    @Test
    public void testReplaceListNestedMap() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("nestedKey", "nestedValue");
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("key", map);
        Mockito.when(objectMapper.writeValueAsBytes(dataMap)).thenReturn(new byte[1]);
        Mockito.when(objectMapper.readValue(new byte[1], Map.class)).thenReturn(dataMap);
        Object result = parametersUtils.replace("${key.nestedKey}", dataMap);
        assertEquals("nestedValue", result);
    }

    @Test
    public void testGetTaskInputV1() throws Exception {
        // ...
    }

    @Test
    public void testGetTaskInputV2() throws Exception {
        // ...
    }

    @Test
    public void testGetWorkflowInput() {
        // ...
    }

    @Test
    public void testGetTaskInput() {
        //...
    }
}

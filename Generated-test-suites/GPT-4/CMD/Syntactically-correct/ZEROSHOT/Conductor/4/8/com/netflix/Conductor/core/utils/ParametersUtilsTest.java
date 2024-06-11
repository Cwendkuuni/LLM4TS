
package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testClone() throws IOException {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key", "value");

        when(objectMapper.writeValueAsBytes(inputTemplate)).thenReturn(new byte[0]);
        when(objectMapper.readValue(new byte[0], parametersUtils.map)).thenReturn(inputTemplate);

        Map<String, Object> result = parametersUtils.clone(inputTemplate);

        assertEquals(result, inputTemplate);
    }

    @Test
    public void testReplace() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        Map<String, Object> input = new HashMap<>();
        input.put("name", "${name}");

        String json = "{\"name\": \"abc\"}";

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertNotNull(result);
        assertEquals("abc", result.get("name"));
    }

    @Test
    public void testReplaceList() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        List<String> values = Arrays.asList("${name}", "${age}");

        String json = "{\"name\": \"abc\", \"age\": \"30\"}";

        List<?> result = (List<?>) parametersUtils.replace(values, json);

        assertNotNull(result);
        assertEquals("abc", result.get(0));
        assertEquals("30", result.get(1));
    }

    @Test
    public void testReplaceString() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        String paramString = "${name}";

        Object result = parametersUtils.replace(paramString);

        assertNotNull(result);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "value2");
        
        WorkflowModel workflowMock = mock(WorkflowModel.class);
        TaskDef taskDefinitionMock = mock(TaskDef.class);
        String taskId = "mockTaskId";
        
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowMock, taskDefinitionMock, taskId);
        assertNotNull(result);
    }

    //And so on with all other methods in ParametersUtils class.
    
}

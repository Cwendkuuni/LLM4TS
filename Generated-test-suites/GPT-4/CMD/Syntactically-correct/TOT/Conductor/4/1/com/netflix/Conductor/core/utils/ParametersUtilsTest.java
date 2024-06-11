package com.netflix.conductor.core.utils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInputV2() throws IOException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        
        //Create mock tasks
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        Mockito.when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("TestParam", "TestValue");
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("TestParamTemplate", "TestValueTemplate");
        taskDefinition.setInputTemplate(inputTemplate);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "1");
        
        assertEquals(1, result.size());
        assertTrue(result.containsKey("TestParam"));
        assertEquals("TestValue", result.get("TestParam"));
    }

    @Test
    public void testReplace() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${value}");
        Map<String, String> json = new HashMap<>();
        json.put("value", "replacedValue");

        Map<String, Object> output = parametersUtils.replace(input, json);
        
        assertEquals(1, output.size());
        assertTrue(output.containsKey("key1"));
        assertEquals("replacedValue", output.get("key1"));
    }

}
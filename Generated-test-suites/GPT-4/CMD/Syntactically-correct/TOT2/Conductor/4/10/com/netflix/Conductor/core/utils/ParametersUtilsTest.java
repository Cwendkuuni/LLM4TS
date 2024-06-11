package com.netflix.conductor.core.utils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void replaceTest() throws Exception {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "value");
        Object json = "json";
        try {
            parametersUtils.replace(input, json);
            fail("Exception not thrown for invalid json object");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void replaceStringTest() {
        String replaced = (String) parametersUtils.replace("${key}");
        assertEquals("", replaced);
    }

    @Test
    public void getTaskInputTest() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "ABC123";
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertEquals(inputParams, taskInput);
    }
    
    //... repeat for other methods ...
    
    @Test
    public void getWorkflowInputTest() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(inputParams, workflowInput);
    }
}
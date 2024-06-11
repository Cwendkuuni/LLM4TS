package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskDef taskDef;

    @Mock
    private WorkflowDef workflowDef;
   
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void getTaskInputTest() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "1");
        assertNotNull(result);
    }
    
    @Test
    public void getTaskInputV2Test() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "1", taskDef);
        assertNotNull(result);
    }

    @Test
    public void getWorkflowInputTest() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(result);
    }
    
    @Test
    public void replaceOneParamTest() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        Map<String, Object> result = parametersUtils.replace(inputParams, "1");
        assertNotNull(result);
    }
    
    @Test
    public void replaceTwoParamsTest() {
        String replaced = (String) parametersUtils.replace("1");
        assertNotNull(replaced);
    }
}
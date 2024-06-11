package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.test.base.ConductorTestBase;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest extends ConductorTestBase {
    // Init ObjectMapper
    private ObjectMapper objectMapper = new ObjectMapper();
    // Init ParametersUtils
    private ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testReplace(){
        Map<String, Object> inputParams = new HashMap();
        inputParams.put("strParam", "stringValue");
        inputParams.put("intParam", 42);
        assertEquals(inputParams, parametersUtils.replace(inputParams,"JsonString"));
    }

    @Test
    public void testReplaceVariables(){
        Map<String, Object> replacement = new HashMap();
        replacement.put("strParam", "stringValue");
        replacement.put("intParam", 42);
        assertEquals("${strParam}", parametersUtils.replace("${strParam}"));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap();
        inputParams.put("strParam", "stringValue");
        inputParams.put("intParam", 42);
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        assertNotNull(parametersUtils.getTaskInputV2(inputParams ,workflow, "taskId", taskDefinition));
    }

    @Test
    public void testReplaceList() {
        Map<String, Object> inputParams = new HashMap();
        inputParams.put("strParam", "stringValue");
        inputParams.put("intParam", 42);
        assertNotNull(parametersUtils.replaceList(Collections.singletonList(inputParams), "taskId", JsonPath.parse(inputParams)));
    }

    @Test
    public void testGetWorkflowInput(){
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap();
        inputParams.put("strParam", "stringValue");
        inputParams.put("intParam", 42);
        workflowDef.setInputTemplate(inputParams);
        assertEquals(inputParams, parametersUtils.getWorkflowInput(workflowDef,inputParams));
    }
}
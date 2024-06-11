package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.apache.commons.cli.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TaskDef taskDefinition;

    @Mock
    private WorkflowDef workflowDef;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskModel taskModel;

    @Mock
    private WorkflowModel workflowModelWithTasks;

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        String taskId = UUID.randomUUID().toString();

        // Test case 1: Input params is null and taskDefinition is null
        Map<String, Object> expectedResult1 = new HashMap<>();
        Map<String, Object> inputParams1 = null;
        WorkflowModel workflow1 = getWorkflowModel();
        assertNull(parametersUtils.getTaskInput(inputParams1, workflow1, null, taskId));

        // Test case 2: Input params and taskDefinition are null
        Map<String, Object> inputParams2 = null;
        TaskDef taskDefinition2 = null;
        WorkflowModel workflow2 = getWorkflowModel();
        assertNull(parametersUtils.getTaskInput(inputParams2, workflow2, taskDefinition2, taskId));

        // Test case 3: Input params and taskDefinition are present
        String inputParamKey1 = "inputParam1";
        String inputParamValue1 = "inputParamValue1";
        String taskInputTemplateKey1 = "taskInputTemplateKey1";
        String taskInputTemplateValue1 = "taskInputTemplateValue1";
        Map<String, Object> inputParams3 = new HashMap<>();
        inputParams3.put(inputParamKey1, inputParamValue1);
        Map<String, Object> taskInputTemplate3 = new HashMap<>();
        taskInputTemplate3.put(taskInputTemplateKey1, taskInputTemplateValue1);
        Mockito.when(taskDefinition.getInputTemplate()).thenReturn(taskInputTemplate3);
        Map<String, Object> expectedResult3 = new HashMap<>();
        expectedResult3.put(inputParamKey1, taskInputTemplateValue1);
        assertEquals(expectedResult3, parametersUtils.getTaskInput(inputParams3, workflowModel, taskDefinition, taskId));

        // Test case 4: Input params are present, but taskDefinition is null
        Map<String, Object> inputParams4 = new HashMap<>();
        inputParams4.put(inputParamKey1, inputParamValue1);
        assertNull(parametersUtils.getTaskInput(inputParams4, workflowModel, null, taskId));

        // Test case 5: Input params are null, but taskDefinition is present
        Map<String, Object> taskInputTemplate4 = new HashMap<>();
        taskInputTemplate4.put(inputParamKey1, inputParamValue1);
        Mockito.when(taskDefinition.getInputTemplate()).thenReturn(taskInputTemplate4);
        Map<String, Object> expectedResult4 = new HashMap<>();
        expectedResult4.put(inputParamKey1, inputParamValue1);
        assertEquals(expectedResult4, parametersUtils.getTaskInput(null, workflowModel, taskDefinition, taskId));

        // Test case 6: Input params and taskDefinition are present, and inputTemplate key is absent
        Map<String, Object> inputParams5 = new HashMap<>();
        inputParams5.put(inputParamKey1, inputParamValue1);
        Map<String, Object> expectedResult5 = new HashMap<>(inputParams5);
        assertEquals(expectedResult5, parametersUtils.getTaskInput(inputParams5, workflowModel, taskDefinition, taskId));

        // Test case 7: Input params and taskDefinition are present, and inputTemplate key is present
        String taskInputTemplateKey2 = "taskInputTemplateKey2";
        String taskInputTemplateValue2 = "taskInputTemplateValue2";
        Map<String, Object> taskInputTemplate5 = new HashMap<>();
        taskInputTemplate5.put(taskInputTemplateKey1, taskInputTemplateValue1);
        taskInputTemplate5.put(taskInputTemplateKey2, taskInputTemplateValue2);
        Mockito.when(taskDefinition.getInputTemplate()).thenReturn(taskInputTemplate5);
        Map<String, Object> expectedResult5 = new HashMap<>(inputParams5);
        expectedResult5.putAll(taskInputTemplate5);
        assertEquals(expectedResult5, parametersUtils.getTaskInput(inputParams5, workflowModel, taskDefinition, taskId));
    }

    @Test
    public void testGetTaskInputV2() {
        String taskId = UUID.randomUUID().toString();

        // Test case 1: Input params and workflow are present, but taskDefinition is null
        String workflowId = UUID.randomUUID().toString();
        String taskDefinitionName = "taskDefinitionName";
        Map<String, Object> inputParams1 = new HashMap<>();
        Map<String, Object> workflow1 = new HashMap<>();
        workflow1.put("input", Map.of("inputParam1", "inputParamValue1"));
        workflow1.put("tasks", List.of(getTaskModel(workflowId, taskDefinitionName)));
        Map<String, Object> expectedResult1 = new HashMap<>();
        expectedResult1.put("inputParam1", "inputParamValue1");
        assertEquals(expectedResult1, parametersUtils.getTaskInputV2(inputParams1, workflow1, taskId, null));

        // Test case 2: Input params, workflow, and taskDefinition are present
        String taskInputTemplateKey1 = "taskInputTemplateKey1";
        String taskInputTemplateValue1 = "taskInputTemplateValue1";
        Map<String, Object> taskInputTemplate2 = new HashMap<>();
        taskInputTemplate2.put(taskInputTemplateKey1, taskInputTemplateValue1);
        Map<String, Object> taskDefinition2 = new HashMap<>();
        taskDefinition2.put("inputTemplate", taskInputTemplate2);
        Map<String, Object> expectedResult2 = new HashMap<>();
        expectedResult2.putAll(inputParams1);
        expectedResult2.putAll(taskInputTemplate2);
        assertEquals(expectedResult2, parametersUtils.getTaskInputV2(inputParams1, workflow1, taskId, taskDefinition2));

        // Test case 3: Input params are null, but workflow and taskDefinition are present
        Map<String, Object> taskInputTemplate3 = new HashMap<>();
        taskInputTemplate3.put(taskInputTemplateKey1, taskInputTemplateValue1);
        Map<String, Object> workflow2 = new HashMap<>();
        workflow2.put("input", Map.of("inputParam1", "inputParamValue1"));
        workflow2.put("tasks", List.of(getTaskModel(workflowId, taskDefinitionName), taskDefinition2));
        Map<String, Object> expectedResult3 = new HashMap<>();
        expectedResult3.putAll(taskInputTemplate3);
        assertEquals(expectedResult3, parametersUtils.getTaskInputV2(null, workflow2, taskId, null));

        // Test case 4: Input params, workflow, and taskDefinition are present, but workflow has no input or tasks
        Map<String, Object> workflow3 = new HashMap<>();
        Map<String, Object> expectedResult4 = new HashMap<>();
        assertEquals(expectedResult4, parametersUtils.getTaskInputV2(inputParams1, workflow3, taskId, taskDefinition2));

        // Test case 5: Input params, workflow, and taskDefinition are present, but workflow has tasks, but no task definition matches the taskId
        String taskRefName = "taskRefName";
        Map<String, Object> taskModel1 = getTaskModel(workflowId, taskDefinitionName);
        Map<String, Object> taskModel2 = getTaskModel(workflowId, taskRefName);
        WorkflowModel workflow4 = new HashMap<>();
        workflow4.put("input", Map.of("inputParam1", "inputParamValue1"));
        workflow4.put("tasks", List.of(taskModel1, taskModel2));
        Map<String, Object> expectedResult5 = new HashMap<>();
        assertEquals(expectedResult5, parametersUtils.getTaskInputV2(inputParams1, workflow4, taskId, taskDefinition2));

        // Test case 6: Input params, workflow, and taskDefinition are present, but workflow has tasks, and taskDefinition matches the taskId, but inputTemplate is null
        Map<String, Object> expectedResult6 = new HashMap<>();
        expectedResult6.putAll(inputParams1);
        assertEquals(expectedResult6, parametersUtils.getTaskInputV2(inputParams1, workflow1, taskId, taskDefinition2));

        // Test case 7: Input params, workflow, and taskDefinition are present, but workflow has tasks, and taskDefinition matches the taskId, and inputTemplate is not null
        Map<String, Object> taskInputTemplate4 = new HashMap<>();
        taskInputTemplate4.put(taskInputTemplateKey1, taskInputTemplateValue1);
        Map<String, Object> taskDefinition4 = new HashMap<>();
        taskDefinition4.put("inputTemplate", taskInputTemplate4);
        Map<String, Object> expectedResult7 = new HashMap<>();
        expectedResult7.putAll(inputParams1);
        expectedResult7.putAll(taskInputTemplate4);
        assertEquals(expectedResult7, parametersUtils.getTaskInputV2(inputParams1, workflow1, taskId, taskDefinition4));
    }

    private Map<String, Object> getTaskModel(String workflowId, String taskDefinitionName) {
        Map<String, Object> taskModel = new HashMap<>();
        taskModel.put("taskType", taskDefinitionName);
        taskModel.put("inputData", Map.of("inputParam1", "inputParamValue1"));
        taskModel.put("outputData", Map.of("outputParam1", "outputParamValue1"));
        return taskModel;
    }

    private WorkflowModel getWorkflowModel() {
        Map<String, Object> workflowModel = new HashMap<>();
        workflowModel.put("input", Map.of("inputParam1", "inputParamValue1"));
        return workflowModel;
    }
}
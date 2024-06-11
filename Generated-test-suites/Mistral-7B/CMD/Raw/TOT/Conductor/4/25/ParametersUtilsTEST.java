package com.netflix.conductor.core.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.model.TaskModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.WorkflowModel;

@ExtendWith(MockitoExtension.class)
public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ParametersUtils parametersUtils;

    @BeforeEach
    public void setup() {
        when(objectMapper.readValue(any(byte[].class), any(TypeReference.class)))
                .thenReturn(new LinkedHashMap<>());
        when(objectMapper.writeValueAsBytes(any()))
                .thenReturn(new byte[0]);
    }

    @DisplayName("Test getTaskInput method with valid input params, workflow and task definition")
    @Test
    public void testGetTaskInput_ValidInputParams_ValidWorkflowAndTaskDefinition() throws Exception {
        WorkflowModel workflowModel = createWorkflowModel();
        TaskDef taskDef = createTaskDef();
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        assertNotNull(result);
        assertEquals(12, result.size());
        assertEquals("value1", result.get("workflow.input.param1"));
        assertEquals("value2", result.get("task.input.param2"));
    }

    @DisplayName("Test getTaskInput method with null workflow")
    @Test
    public void testGetTaskInput_NullWorkflow() {
        Map<String, Object> inputParams = new HashMap<>();
        TaskDef taskDef = createTaskDef();
        WorkflowModel workflowModel = null;

        assertThrows(IllegalArgumentException.class, () -> {
            parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
        });
    }

    @DisplayName("Test getTaskInput method with null task definition")
    @Test
    public void testGetTaskInput_NullTaskDefinition() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = createWorkflowModel();
        TaskDef taskDef = null;

        assertThrows(IllegalArgumentException.class, () -> {
            parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
        });
    }

    @DisplayName("Test getTaskInput method with empty input params")
    @Test
    public void testGetTaskInput_EmptyInputParams() {
        WorkflowModel workflowModel = createWorkflowModel();
        TaskDef taskDef = createTaskDef();
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @DisplayName("Test getTaskInput method with invalid workflow schema version")
    @Test
    public void testGetTaskInput_InvalidWorkflowSchemaVersion() {
        WorkflowModel workflowModel = createWorkflowModelWithInvalidSchemaVersion();
        TaskDef taskDef = createTaskDef();
        Map<String, Object> inputParams = new HashMap<>();

        assertThrows(UnsupportedOperationException.class, () -> {
            parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
        });
    }

    @DisplayName("Test replace method with valid JSON string")
    @Test
    public void testReplace_ValidJsonString() throws Exception {
        String jsonString = "{\"input\":{\"param1\":\"value1\",\"param2\":[{\"subParam\":\"subValue\"}]}}";
        DocumentContext documentContext = mock(DocumentContext.class);

        Map<String, Object> result = parametersUtils.replace(jsonString, documentContext, "taskId");

        assertEquals("value1", result.get("input.param1"));
        assertEquals("subValue", result.get("input.param2[0].subParam"));
    }

    @DisplayName("Test replace method with null JSON string")
    @Test
    public void testReplace_NullJsonString() {
        DocumentContext documentContext = mock(DocumentContext.class);
        String jsonString = null;

        Map<String, Object> result = parametersUtils.replace(jsonString, documentContext, "taskId");

        assertNull(result);
    }

    @DisplayName("Test replace method with empty JSON string")
    @Test
    public void testReplace_EmptyJsonString() throws Exception {
        String jsonString = "";
        DocumentContext documentContext = mock(DocumentContext.class);

        Map<String, Object> result = parametersUtils.replace(jsonString, documentContext, "taskId");

        assertEquals(0, result.size());
    }

    @DisplayName("Test replaceList method with valid list")
    @Test
    public void testReplaceList_ValidList() throws Exception {
        List<Object> list = Collections.singletonList("value1");
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        List<Object> result = parametersUtils.replaceList(list, taskId, documentContext);

        assertEquals("value1", result.get(0));
    }

    @DisplayName("Test replaceList method with empty list")
    @Test
    public void testReplaceList_EmptyList() throws Exception {
        List<Object> list = Collections.emptyList();
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        List<Object> result = parametersUtils.replaceList(list, taskId, documentContext);

        assertEquals(0, result.size());
    }

    @DisplayName("Test replaceVariables method with valid string")
    @Test
    public void testReplaceVariables_ValidString() throws Exception {
        String paramString = "${workflow.input.param1}.${task.output.param2}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        String result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertEquals("value1.value3", result);
    }

    @DisplayName("Test replaceVariables method with null JSONContext")
    @Test
    public void testReplaceVariables_NullJsonContext() throws Exception {
        String paramString = "${workflow.input.param1}.${task.output.param2}";
        DocumentContext documentContext = null;
        String taskId = "taskId";

        String result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertEquals(paramString, result);
    }

    @DisplayName("Test replaceVariables method with empty string")
    @Test
    public void testReplaceVariables_EmptyString() throws Exception {
        String paramString = "${workflow.input.param1}.${task.output.param2}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        String result = parametersUtils.replaceVariables("", documentContext, taskId);

        assertEquals("", result);
    }

    @DisplayName("Test replaceVariables method with invalid JSONPath")
    @Test
    public void testReplaceVariables_InvalidJsonPath() throws Exception {
        String paramString = "${workflow.input.param1}.${task.output.param2}.";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        String result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertEquals(paramString, result);
    }

    private Map<String, Object> createWorkflowModel() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(2);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);
        workflowModel.setInput(new HashMap<>());
        workflowModel.setTasks(Collections.singletonList(createTaskModel()));

        return workflowModel;
    }

    private Map<String, Object> createTaskModel() {
        TaskDef taskDef = createTaskDef();
        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName("taskReferenceName");
        taskModel.setTaskDefName("taskDefName");
        taskModel.setInputData(new HashMap<>());
        taskModel.setOutputData(new HashMap<>());
        taskModel.setStatus(null);

        return taskModel;
    }

    private TaskDef createTaskDef() {
        TaskDef taskDef = mock(TaskDef.class);
        when(taskDef.getInputTemplate()).thenReturn(new HashMap<>());

        return taskDef;
    }

    private WorkflowModel createWorkflowModelWithInvalidSchemaVersion() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(-1);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);

        return workflowModel;
    }
}
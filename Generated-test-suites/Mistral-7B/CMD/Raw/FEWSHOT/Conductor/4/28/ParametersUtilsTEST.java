package com.netflix.conductor.core.utils;

import static com.netflix.conductor.core.utils.ParametersUtils.clone;
import static com.netflix.conductor.core.utils.ParametersUtils.getTaskInput;
import static com.netflix.conductor.core.utils.ParametersUtils.getTaskInputV2;
import static com.netflix.conductor.core.utils.ParametersUtils.replace;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowInstance;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.workflowtasks.WorkflowTask;

/**
 * Test class for {@link com.netflix.conductor.core.utils.ParametersUtils}
 */
@Component
public class ParametersUtilsTest {

    private static final String WORKFLOW_JSON =
            "{\n"
                    + "  \"input\": {\n"
                    + "    \"param1\": \"value1\",\n"
                    + "    \"param2\": null,\n"
                    + "    \"param3\": {\n"
                    + "      \"subParam1\": \"subValue1\",\n"
                    + "      \"subParam2\": null\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"output\": {\n"
                    + "    \"outputParam1\": \"outputValue1\",\n"
                    + "    \"outputParam2\": null,\n"
                    + "    \"outputParam3\": {\n"
                    + "      \"subOutputParam1\": \"subOutputValue1\",\n"
                    + "      \"subOutputParam2\": null\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"status\": \"RUNNING\",\n"
                    + "  \"workflowId\": \"workflowId1\",\n"
                    + "  \"parentWorkflowId\": null,\n"
                    + "  \"parentWorkflowTaskId\": null,\n"
                    + "  \"workflowType\": \"workflowType1\",\n"
                    + "  \"version\": 1,\n"
                    + "  \"correlationId\": \"correlationId1\",\n"
                    + "  \"reasonForIncompletion\": null,\n"
                    + "  \"variables\": {\n"
                    + "    \"variable1\": \"variableValue1\"\n"
                    + "  },\n"
                    + "  \"tasks\": [\n"
                    + "    {\n"
                    + "      \"taskId\": \"taskId1\",\n"
                    + "      \"referenceTaskName\": \"taskRef1\",\n"
                    + "      \"status\": null,\n"
                    + "      \"retryCount\": 0,\n"
                    + "      \"correlationId\": \"correlationId1\",\n"
                    + "      \"pollCount\": 0,\n"
                    + "      \"taskDefName\": \"taskDef1\",\n"
                    + "      \"scheduledTime\": null,\n"
                    + "      \"startTime\": null,\n"
                    + "      \"endTime\": null,\n"
                    + "      \"workflowInstanceId\": null,\n"
                    + "      \"workflowTaskId\": null,\n"
                    + "      \"reasonForIncompletion\": null,\n"
                    + "      \"inputData\": {\n"
                    + "        \"inputParam1\": \"inputValue1\",\n"
                    + "        \"inputParam2\": null,\n"
                    + "        \"inputParam3\": {\n"
                    + "          \"subInputParam1\": \"subInputValue1\",\n"
                    + "          \"subInputParam2\": null\n"
                    + "        }\n"
                    + "      },\n"
                    + "      \"outputData\": {\n"
                    + "        \"outputParam1\": \"outputValue1\",\n"
                    + "        \"outputParam2\": null,\n"
                    + "        \"outputParam3\": {\n"
                    + "          \"subOutputParam1\": \"subOutputValue1\",\n"
                    + "          \"subOutputParam2\": null\n"
                    + "        }\n"
                    + "      }\n"
                    + "    }\n"
                    + "  ]\n"
                    + "}";

    private static final String TASK_DEF_JSON =
            "{\n"
                    + "  \"inputTemplate\": {\n"
                    + "    \"param1\": \"${workflow.input.param1}\",\n"
                    + "    \"param2\": \"${workflow.input.param3.subParam1}\"\n"
                    + "  },\n"
                    + "  \"taskType\": \"TaskType1\",\n"
                    + "  \"definition\": \"TaskDef1\"\n"
                    + "}";

    private static final String TASK_INPUT_JSON = "{\"inputParam1\": \"inputValue2\"}";

    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private ParametersUtils parametersUtils;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        workflowDef = objectMapper.readValue(new ObjectMapper().writeValueAsBytes(new WorkflowModel()), WorkflowDef.class);
        taskDef = objectMapper.readValue(new ObjectMapper().writeValueAsBytes(new TaskDef("taskDefName")), TaskDef.class);
        when(taskDef.getInputTemplate()).thenReturn(new HashMap<>());
        when(taskDef.getInputTemplate()).thenReturn(new HashMap<>(Map.of("param1", "${workflow.input.param1}")));
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV1() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "inputValue1");
        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowDef, inputParams);
        assertEquals("value1", result.get("param1"));
        assertNull(result.get("param2"));
        assertNull(result.get("param3"));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "inputValue1");
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowInstance workflowInstance = new WorkflowInstance();
        WorkflowTask workflowTask = new WorkflowTask();
        workflowModel.setInput(workflowDef.getInput());
        workflowInstance.setWorkflow(workflowModel);
        workflowTask.setWorkflowInstanceId(workflowInstance.getId());
        workflowTask.setTaskId("taskId2");
        WorkflowTask task = new WorkflowTask();
        task.setWorkflowInstanceId(workflowInstance.getId());
        task.setTaskId("taskId1");
        workflowInstance.setTasks(List.of(workflowTask, task));
        Map<String, Object> result =
                parametersUtils.getTaskInputV2(inputParams, workflowDef, "taskId1", taskDef);
        assertEquals("inputValue1", result.get("param1"));
        assertEquals("subValue1", result.get("param3.subParam1"));
        assertNull(result.get("param2"));
        assertNull(result.get("param3.subParam2"));
    }

    @Test
    public void testReplace() {
        String original = "${workflow.input.param1} ${workflow.input.param3.subParam1}";
        String expected = "inputValue1 subValue1";
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "inputValue1");
        inputParams.put("param3.subParam1", "subValue1");
        Map<String, Object> result = replace(inputParams, workflowDef);
        assertEquals(expected, result.toString());
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.param3.subParam1} ${workflow.input.param3.subParam2}";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());
        Object replacement = documentContext.read("$.input.param3.subParam1");
        String expected = replacement.toString();
        Map<String, Object> result = replaceVariables(paramString, documentContext, null);
        assertEquals(expected, result.toString());
    }

    @Test
    public void testReplaceList() {
        String json = "[\n" + "  ${workflow.input.param3.subParam1},\n" + "  ${workflow.input.param3.subParam2}\n" + "]";
        List<Object> expected = List.of("subValue1", "subValue2");
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());
        documentContext.read("$.input.param3.subParam1");
        documentContext.read("$.input.param3.subParam2");
        Map<String, Object> result = replaceList(Collections.emptyList(), null, documentContext);
        assertEquals(expected, result.toString());
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "inputValue1");
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals("inputValue1", result.get("param1"));
        assertNull(result.get("param2"));
    }
}
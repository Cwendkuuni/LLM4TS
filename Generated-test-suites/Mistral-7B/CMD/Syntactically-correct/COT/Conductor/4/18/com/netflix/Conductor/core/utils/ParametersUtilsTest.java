import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowInstanceStatus;
import com.netflix.conductor.utils.ParametersUtils;

@Component
public class ParametersUtilsTest {

    private static final String WORKFLOW_NAME = "workflowName";
    private static final String WORKFLOW_VERSION = "1.0.0";
    private static final String TASK_DEF_NAME = "taskDefName";
    private static final String TASK_INPUT = "taskInput";
    private static final String TASK_OUTPUT = "taskOutput";
    private static final String TASK_ID = "taskId";
    private static final String WORKFLOW_ID = "workflowId";
    private static final String PARENT_WORKFLOW_ID = "parentWorkflowId";
    private static final String PARENT_WORKFLOW_TASK_ID = "parentWorkflowTaskId";
    private static final String VARIABLE = "variable";

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;
    private Map<String, Object> inputParams;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
        workflowDef = mock(WorkflowDef.class);
        taskDef = mock(TaskDef.class);
        workflowModel = new WorkflowModel();
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setWorkflowVersion(WORKFLOW_VERSION);
        inputParams = new HashMap<>();
    }

    @Test
    public void testGetTaskInput_NullInputParams_ReturnsEmptyMap() {
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());

        Map<String, Object> output = parametersUtils.getTaskInput(null, workflowModel, taskDef, TASK_ID);
        assertEquals(Collections.emptyMap(), output);
    }

    @Test
    public void testGetTaskInput_NullWorkflowAndTaskDefinitions_ReturnsEmptyMap() {
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());

        Map<String, Object> output = parametersUtils.getTaskInput(inputParams, null, null, TASK_ID);
        assertEquals(Collections.emptyMap(), output);
    }

    @Test
    public void testGetTaskInput_ValidInputParamsAndDefinitions_ReturnsExpectedMap() {
        setupTestGetTaskInput();

        Map<String, Object> expected = new HashMap<>();
        expected.put("workflow", workflowParams());
        expected.put("task", taskParams());

        when(workflowDef.getInputTemplate()).thenReturn(inputTemplate());

        Map<String, Object> output = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, TASK_ID);

        assertEquals(expected, output);
    }

    @Test
    public void testGetTaskInputV2_NullInputParams_ReturnsEmptyMap() {
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());

        Map<String, Object> output = parametersUtils.getTaskInputV2(null, workflowModel, TASK_ID, taskDef);
        assertEquals(Collections.emptyMap(), output);
    }

    @Test
    public void testGetTaskInputV2_ValidInputParamsAndDefinitions_ReturnsExpectedMap() {
        setupTestGetTaskInputV2();

        Map<String, Object> expected = new HashMap<>();
        expected.put("workflow", workflowParams());
        expected.put("task", taskParams());

        when(workflowDef.getInputTemplate()).thenReturn(inputTemplate());

        Map<String, Object> output = parametersUtils.getTaskInputV2(inputParams, workflowModel, TASK_ID, taskDef);

        assertEquals(expected, output);
    }

    @Test
    public void testGetWorkflowInput_NullWorkflowDef_ReturnsEmptyMap() {
        Map<String, Object> output = parametersUtils.getWorkflowInput(null, inputParams);
        assertEquals(Collections.emptyMap(), output);
    }

    @Test
    public void testGetWorkflowInput_ValidWorkflowDef_ReturnsExpectedMap() {
        setupTestGetWorkflowInput();

        Map<String, Object> output = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(expectedWorkflowInput(), output);
    }

    // Add other test cases for other methods in the class, e.g., replace, replaceVariables, etc.

    private Map<String, Object> workflowParams() {
        Map<String, Object> workflowParams = new HashMap<>();
        workflowParams.put("input", inputTemplate());
        workflowParams.put("output", outputTemplate());
        workflowParams.put("status", WorkflowInstanceStatus.STARTED);
        workflowParams.put("workflowId", WORKFLOW_ID);
        workflowParams.put("parentWorkflowId", PARENT_WORKFLOW_ID);
        workflowParams.put("parentWorkflowTaskId", PARENT_WORKFLOW_TASK_ID);
        workflowParams.put("variables", variables());
        return workflowParams;
    }

    private Map<String, Object> inputTemplate() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("variable", VARIABLE);
        return inputTemplate;
    }

    private Map<String, Object> outputTemplate() {
        Map<String, Object> outputTemplate = new HashMap<>();
        outputTemplate.put("variable", VARIABLE);
        return outputTemplate;
    }

    private Map<String, Object> variables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("variable", VARIABLE);
        return variables;
    }

    private Map<String, Object> taskParams() {
        Map<String, Object> taskParams = new HashMap<>();
        taskParams.put("input", inputDataTemplate());
        taskParams.put("output", outputDataTemplate());
        taskParams.put("taskType", TASK_DEF_NAME);
        taskParams.put("status", WorkflowInstanceStatus.STARTED);
        taskParams.put("referenceTaskName", TASK_DEF_NAME);
        taskParams.put("retryCount", 0);
        taskParams.put("correlationId", TASK_ID);
        taskParams.put("pollCount", 0);
        taskParams.put("taskDefName", TASK_DEF_NAME);
        taskParams.put("scheduledTime", System.currentTimeMillis());
        taskParams.put("startTime", System.currentTimeMillis());
        taskParams.put("endTime", System.currentTimeMillis());
        taskParams.put("workflowInstanceId", WORKFLOW_ID);
        taskParams.put("taskId", TASK_ID);
        taskParams.put(Vars.REASON_FOR_INCOMPLETION, "reason_for_incompletion");
        taskParams.put("callbackAfterSeconds", 0);
        taskParams.put("workerId", "worker_id");
        taskParams.put("iteration", 0);
        return taskParams;
    }

    private Map<String, Object> inputDataTemplate() {
        Map<String, Object> inputDataTemplate = new HashMap<>();
        inputDataTemplate.put(TASK_INPUT, "taskInputValue");
        return inputDataTemplate;
    }

    private Map<String, Object> outputDataTemplate() {
        Map<String, Object> outputDataTemplate = new HashMap<>();
        outputDataTemplate.put(TASK_OUTPUT, "taskOutputValue");
        return outputDataTemplate;
    }

    private Map<String, Object> expectedWorkflowInput() {
        Map<String, Object> expectedWorkflowInput = new HashMap<>();
        expectedWorkflowInput.put("input", inputTemplate());
        expectedWorkflowInput.put("output", outputTemplate());
        return expectedWorkflowInput;
    }

    private void setupTestGetTaskInput() {
        setupTestGetTaskInputV1();
        when(taskDef.getInputTemplate()).thenReturn(Collections.emptyMap());
    }

    private void setupTestGetTaskInputV1() {
        setupTestGetTaskInputV2();
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());
        when(taskDef.getInputTemplate()).thenReturn(inputTemplate());
    }

    private void setupTestGetTaskInputV2() {
        setupTestGetTaskInput();
        when(workflowDef.getInputTemplate()).thenReturn(inputTemplate());
    }
}
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    private EnvUtils envUtils;

    @Mock
    private TaskUtils taskUtils;

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(objectMapper.readValue(anyByteArray(), any(TypeReference.class))).thenReturn(Collections.emptyMap());
    }

    @Test
    public void testGetTaskInput_ValidInput() throws IOException {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "value1");

        when(workflowModel.getTasks().stream().map(TaskModel::getReferenceTaskName).findFirst().isPresent())
                .thenReturn(true);
        when(workflowModel.getTasks().stream().map(TaskModel::getReferenceTaskName).findFirst().get())
                .thenReturn("taskRefName");
        when(workflowModel.getTasks().get(0)).thenReturn(taskModel);
        when(taskModel.getInputData()).thenReturn(input);
        when(taskModel.getOutputData()).thenReturn(Collections.emptyMap());
        when(taskModel.getStatus()).thenReturn(null);
        when(taskModel.getWorkerId()).thenReturn("workerId");
        when(taskModel.getWorkerId()).thenReturn("workerId");
        when(workflowModel.getInput()).thenReturn(Collections.emptyMap());

        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("key1", "value1");
        expectedOutput.put("workflow", workflowModel.getInput());
        expectedOutput.put("taskId", "taskRefName");

        Map<String, Object> result = parametersUtils.getTaskInput(input, workflowModel, taskDefinition, "taskId");

        assertEquals(expectedOutput, result);
        verify(taskDefinition).getInputTemplate();
    }

    @Test
    public void testGetTaskInput_NullInput() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("workflow", workflowModel.getInput());

        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, taskDefinition, "taskId");

        assertEquals(expectedOutput, result);
        verify(taskDefinition, never()).getInputTemplate();
    }

    @Test
    public void testGetTaskInput_NullTaskDefinition() {
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> input = new HashMap<>();

        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("workflow", workflowModel.getInput());

        Map<String, Object> result = parametersUtils.getTaskInput(input, workflowModel, null, "taskId");

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetTaskInput_NullWorkflowModel() {
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> input = new HashMap<>();

        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("workflow", Collections.emptyMap());

        Map<String, Object> result = parametersUtils.getTaskInput(input, null, taskDefinition, "taskId");

        assertEquals(expectedOutput, result);
        verify(taskDefinition, never()).getInputTemplate();
    }

    @Test
    public void testGetTaskInput_NullTaskId() {
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> input = new HashMap<>();
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("workflow", workflowModel.getInput());

        Map<String, Object> result = parametersUtils.getTaskInput(input, workflowModel, taskDefinition, null);

        assertEquals(expectedOutput, result);
        verify(taskDefinition, never()).getInputTemplate();
    }

    // Add more test cases for other methods in the ParametersUtils class, such as getTaskInputV2, replace, etc.
}
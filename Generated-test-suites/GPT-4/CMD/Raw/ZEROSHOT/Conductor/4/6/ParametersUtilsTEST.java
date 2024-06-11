
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.utils.ParametersUtils;

import java.util.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    
    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }


    @Test
    public void testReplaceWithNullInput() {
        Map<String, Object> result = parametersUtils.replace(null, "input");
        assertNull(result);
    }

    @Test
    public void testReplaceWithString() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "value1");
        input.put("key2", "${'value2'}");
        Map<String, Object> result = parametersUtils.replace(input, "anyString");
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "workflow.input.value1");
        inputParams.put("key2", "task1.output.value2");
        
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("value1", "workflowValue");
        when(workflowModel.getInput()).thenReturn(workflowInput);
        
        TaskModel taskModel = mock(TaskModel.class);
        Map<String, Object> taskOutputData = new HashMap<>();
        taskOutputData.put("value2", "taskOutputValue");
        when(taskModel.getOutputData()).thenReturn(taskOutputData);
        when(workflowModel.getTaskByRefName("task1")).thenReturn(taskModel);

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);
        assertEquals("workflowValue", result.get("key1"));
        assertEquals("taskOutputValue", result.get("key2"));
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskDef taskDef = mock(TaskDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "123");
        assertEquals("value", result.get("key"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals("value", result.get("key"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "${'value'}");

        Map<String, Object> result = parametersUtils.replace(input, "{}");
        assertEquals("value", result.get("key"));
    }

    @Test
    public void testReplaceWithEmptyInput() {
        Map<String, Object> result = parametersUtils.replace(new HashMap<>(), "{}");
        assertEquals(0, result.size());
    }

    @Test
    public void testReplaceStringVariable() {
        Object result = parametersUtils.replace("${'hello'}");
        assertEquals("hello", result);
    }
}

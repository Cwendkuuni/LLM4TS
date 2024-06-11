import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV2EdgeCases() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.getWorkflowDefinition().setSchemaVersion(2);

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Map.of("key", "value"));

        Map<String, Object> result = parametersUtils.getTaskInputV2(null, workflowModel, "taskId", taskDef);
        assertEquals(1, result.size());
        assertEquals("value", result.get("key"));
    }

    @Test
    public void testGetTaskInputTypicalUseCase() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.getWorkflowDefinition().setSchemaVersion(2);
        workflowModel.setInput(Map.of("input1", "value1"));
        workflowModel.setOutput(Map.of("output1", "value2"));
        workflowModel.setStatus("COMPLETED");
        workflowModel.setWorkflowId("123");

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskType("task");
        taskModel.setReferenceTaskName("task1");
        taskModel.setInputData(Map.of("taskInput", "taskValue"));

        workflowModel.getTasks().add(taskModel);

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Map.of("key1", "value1"));

        Map<String, Object> result = parametersUtils.getTaskInputV2(null, workflowModel, "taskId", taskDef);
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("taskValue", ((Map<String, Object>) result.get("task1")).get("taskInput"));
    }

    @Test(expected = RuntimeException.class)
    public void testCloneWithIOException() {
        Map<String, Object> inputTemplate = Map.of("key", "value");
        parametersUtils.clone(inputTemplate);
    }

    @Test
    public void testReplaceWithDocumentContext() {
        Map<String, Object> input = Map.of("key", "value");
        Object json = "{}";
        Map<String, Object> result = parametersUtils.replace(input, json);
        assertEquals(1, result.size());
        assertEquals("value", result.get("key"));
    }

    @Test
    public void testReplaceString() {
        String input = "${key}";
        Object result = parametersUtils.replace(input);
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${key}";
        Object result = parametersUtils.replaceVariables(paramString, null, "taskId", 0);
        assertEquals("", result);
    }

    @Test
    public void testReplaceList() {
        String taskId = "taskId";
        Map<String, Object> io = Map.of("key", "value");
        Object result = parametersUtils.replaceList(List.of("${key}"), taskId, io);
        assertEquals(1, ((List<?>) result).size());
        assertEquals("value", ((List<?>) result).get(0));
    }

}
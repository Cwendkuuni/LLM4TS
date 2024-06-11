import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class ParametersUtilsTest {

    ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task1";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(taskInput);
        assertTrue(taskInput.isEmpty());
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${workflow.input.key1}");
        input.put("key2", "${task.input.key2}");

        WorkflowModel workflow = new WorkflowModel();
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("key1", "value1");
        workflow.setInput(workflowInput);

        TaskModel task = new TaskModel();
        Map<String, Object> taskInput = new HashMap<>();
        taskInput.put("key2", "value2");
        task.setInputData(taskInput);

        workflow.addTask(task);

        Map<String, Object> taskInput = parametersUtils.getTaskInput(input, workflow, null, null);

        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("key1"));
        assertEquals("value2", taskInput.get("key2"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "{\"workflow\":{\"key\":\"${workflow.foo}\"}}";
        DocumentContext documentContext = JsonPath.parse("{\"foo\":\"bar\"}");
        String replacedString = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertNotNull(replacedString);
        assertEquals("{\"workflow\":{\"key\":\"bar\"}}", replacedString);
    }

    // Add more test cases as needed for other methods in ParametersUtils

}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflow = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.param1");
        inputParams.put("param2", "task1.output.param2");

        Map<String, Object> taskInput = parametersUtils.getTaskInput(null, workflow, null, null);

        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("param1"));
        assertEquals("value2", taskInput.get("param2"));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.param1");
        inputParams.put("param2", "task1.output.param2");

        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("param1"));
        assertEquals("value2", taskInput.get("param2"));
    }

    @Test
    public void testReplace() {
        String input = "{\"param1\": \"${workflow.input.param1}\", \"param2\": \"${task1.output.param2}\"}";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(Map.of("param1", "value1"));
        workflow.addTask(new TaskModel());
        workflow.getTaskByRefName("task1").setOutputData(Map.of("param2", "value2"));

        Object replacedObject = parametersUtils.replace(input);

        assertNotNull(replacedObject);
        assertEquals("{\"param1\":\"value1\",\"param2\":\"value2\"}", replacedObject.toString());
    }
}
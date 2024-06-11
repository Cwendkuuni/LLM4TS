import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<>());
        
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, "taskId");
        
        assertNotNull(taskInput);
        // Add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        
        Map<String, Object> input = new HashMap<>();
        input.put("testKey", "testValue");
        
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(input, workflow, "taskId", null);
        
        assertNotNull(taskInput);
        // Add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("testKey", "${testValue}");
        
        Object replaced = parametersUtils.replace(input, "{\"testValue\": \"replacedValue\"}");
        
        assertNotNull(replaced);
        // Add more assertions for typical use cases, edge cases, and error scenarios
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "Hello, ${name}!";
        WorkflowModel workflow = new WorkflowModel();
        workflow.getInput().put("name", "Alice");
        
        Object replaced = parametersUtils.replaceVariables(paramString, workflow, "taskId");
        
        assertNotNull(replaced);
        assertEquals("Hello, Alice!", replaced.toString());
        // Add more assertions for typical use cases, edge cases, and error scenarios
    }

}
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        taskDef.setName("testTask");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "value2");

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, "taskId");

        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("param1"));
        assertEquals("value2", taskInput.get("param2"));
    }

    @Test
    public void testReplaceVariables() {
        String testString = "Hello, ${name}";
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("name", "Alice");

        Object result = parametersUtils.replaceVariables(testString, documentContext, "taskId");

        assertNotNull(result);
        assertEquals("Hello, Alice", result);
    }

    @Test
    public void testReplaceVariables_EmptyValue() {
        String testString = "Hello, ${}";
        Map<String, Object> documentContext = new HashMap<>();

        Object result = parametersUtils.replaceVariables(testString, documentContext, "taskId");

        assertNotNull(result);
        assertEquals("Hello, ", result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("key", "value"));

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals("value1", workflowInput.get("param1"));
        assertEquals("value", workflowInput.get("key"));
    }

    @Test
    public void testReplaceVariables_NullValue() {
        String testString = "Hello, ${undefined}";
        Map<String, Object> documentContext = new HashMap<>();

        Object result = parametersUtils.replaceVariables(testString, documentContext, "taskId");

        assertNotNull(result);
        assertEquals("Hello, null", result);
    }

    @Test
    public void testGetTaskInputV2_NullInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("key", "value"));

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(null, workflow, "taskId", taskDef);

        assertNotNull(taskInput);
        assertEquals("value", taskInput.get("key"));
    }

    @Test
    public void testGetTaskInputV2_EmptyInputParams() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("key", "value"));

        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, "taskId", taskDef);

        assertNotNull(taskInput);
        assertEquals("value", taskInput.get("key"));
    }

    @Test
    public void testReplace_NullValue() {
        Map<String, Object> input = Collections.singletonMap("key", "${undefined}");
        Object json = "{\"key\": null}";

        Map<String, Object> replacedMap = parametersUtils.replace(input, json);

        assertNotNull(replacedMap);
        assertNull(replacedMap.get("key"));
    }

}
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        // Typical use case
        ParametersUtils parametersUtils = new ParametersUtils(mock(ObjectMapper.class));
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDef = mock(TaskDef.class);
        String taskId = "123";
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, taskId);
        assertNotNull(taskInput);
        
        // Edge case
        inputParams.put("param1", "value1");
        inputParams.put("param2", 123);
        taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, taskId);
        assertNotNull(taskInput);
        
        // Error scenario
        taskInput = parametersUtils.getTaskInput(null, null, null, null);
        assertEquals(0, taskInput.size());
    }

    @Test
    public void testGetTaskInputV2() {
        // Typical use case
        ParametersUtils parametersUtils = new ParametersUtils(mock(ObjectMapper.class));
        WorkflowModel workflow = mock(WorkflowModel.class);
        String taskId = "123";
        TaskDef taskDef = mock(TaskDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDef);
        assertNotNull(taskInput);
        
        // Edge case
        taskInput = parametersUtils.getTaskInputV2(null, null, null, null);
        assertNotNull(taskInput);
        
        // Error scenario
        taskInput = parametersUtils.getTaskInputV2(null, null, null, taskDef);
        assertNotNull(taskInput);
    }

    @Test
    public void testReplace() {
        // Typical use case
        ParametersUtils parametersUtils = new ParametersUtils(mock(ObjectMapper.class));
        String jsonString = "{\"key\": \"${value}\"}";
        Object replaced = parametersUtils.replace(jsonString);
        assertNotNull(replaced);
        
        // Edge case
        jsonString = "{\"key\": \"${}\"}";
        replaced = parametersUtils.replace(jsonString);
        assertNotNull(replaced);
        
        // Error scenario
        jsonString = "{\"key\": \"value\"}";
        replaced = parametersUtils.replace(jsonString);
        assertNotNull(replaced);
    }

    @Test
    public void testReplaceVariables() {
        // Typical use case
        ParametersUtils parametersUtils = new ParametersUtils(mock(ObjectMapper.class));
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "123";
        String paramString = "${workflow.input.key}";
        Object replaced = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        assertNotNull(replaced);
        
        // Edge case
        paramString = "${}";
        replaced = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        assertNotNull(replaced);
        
        // Error scenario
        paramString = "${invalid.expression}";
        replaced = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        assertNotNull(replaced);
    }

}
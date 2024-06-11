import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.setOutput(new HashMap<>());
        workflow.setStatus("COMPLETED");
        workflow.setWorkflowId("123456");
        workflow.setParentWorkflowId("7890");
        // Add more workflow attributes for testing
        
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<>());
        // Add more task definition attributes for testing
        
        String taskId = "task123";
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "value2");
        
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, taskId);
        
        // Assert the expected taskInput values based on inputParams, workflow, and taskDef
        
    }
    
    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "value1");
        input.put("key2", "value2");
        
        Object json = Collections.singletonMap("key3", "value3");
        
        Map<String, Object> replacedMap = parametersUtils.replace(input, json);
        
        // Assert the expected values in the replacedMap
        
    }
    
    @Test
    public void testReplaceVariables() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("key", "jsonValue"));
        
        String paramString = "${key}";
        
        Object replacedValue = parametersUtils.replaceVariables(paramString, documentContext, null);
        
        // Assert the expected replacedValue
        
    }
    
    // Add more test methods to cover all scenarios in ParametersUtils class
    
}
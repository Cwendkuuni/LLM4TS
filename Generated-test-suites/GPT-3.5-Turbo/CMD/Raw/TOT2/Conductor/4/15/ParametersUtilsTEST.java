import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {
    
    private ParametersUtils parametersUtils;
    
    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }
    
    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.setOutput(new HashMap<>());
        workflow.setStatus("RUNNING");
        workflow.setWorkflowId("12345");
        // Set other values for testing
        
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<>());
        // Set other values for testing
        
        Map<String, Object> inputParams = new HashMap<>();
        // Add input parameters for testing
        
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, "task1", taskDef);
        assertNotNull(taskInput);
        // Add assertions for the expected values
    }
    
    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        // Add input data for testing
        
        Map<String, Object> data = new HashMap<>();
        // Add data for testing
        
        Object result = parametersUtils.replace(input, data);
        assertNotNull(result);
        // Add assertions for the expected values
    }
    
    @Test
    public void testReplaceVariables() {
        String paramString = "${key}";
        DocumentContext documentContext = JsonPath.parse(new HashMap<>());
        String taskId = "task1";
        
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        assertNotNull(result);
        // Add assertions for the expected values
    }
    
    // Add more test methods for other public methods in ParametersUtils class
    
}
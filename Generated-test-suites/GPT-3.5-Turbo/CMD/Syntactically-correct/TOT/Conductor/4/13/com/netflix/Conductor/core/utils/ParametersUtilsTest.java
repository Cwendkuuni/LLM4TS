import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.param1");
        
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");
        
        assertTrue(result.containsKey("param1"));
        assertEquals("workflow.param1", result.get("param1"));
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.param1");
        
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, "taskId", taskDefinition);
        
        assertTrue(result.containsKey("param1"));
        assertEquals("workflow.param1", result.get("param1"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.param1}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        
        assertEquals("workflow.param1", result);
    }

    // Add more test cases for other methods...

}
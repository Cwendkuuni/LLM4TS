import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(workflow.getWorkflowName()).thenReturn("TestWorkflow");
        when(workflow.getWorkflowVersion()).thenReturn(1);

        TaskDef taskDef = mock(TaskDef.class);
        when(taskDef.getInputTemplate()).thenReturn(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, "taskId", taskDef);

        assertNotNull(taskInput);
    }

    @Test
    public void testReplaceVariables() {
        Map<String, Object> input = new HashMap<>();
        input.put("param1", "test");
        input.put("param2", "${workflow.input.param1}");

        Map<String, Object> workflowParams = new HashMap<>();
        workflowParams.put("param1", "workflowTest");

        DocumentContext documentContext = JsonPath.parse(workflowParams);
        
        String replacedParam2 = (String) parametersUtils.replaceVariables((String) input.get("param2"), documentContext, null);

        assertEquals("workflowTest", replacedParam2);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "test");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
    }
}
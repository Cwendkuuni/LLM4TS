import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "12345";

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "${workflow.input.param}");

        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("param", "workflowParam");

        workflow.setInput(workflowInput);

        Map<String, Object> expected = new HashMap<>();
        expected.put("param1", "value1");
        expected.put("param2", "workflowParam");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDef, taskId);

        assertEquals(expected, result);
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        Map<String, Object> input = new HashMap<>();
        input.put("param1", "${workflow.input.param1}");
        input.put("param2", "${task.input.param2}");
        input.put("param3", "constant");

        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$workflow.input.param1")).thenReturn("value1");
        when(documentContext.read("$task.input.param2")).thenReturn("value2");

        Map<String, Object> expected = new HashMap<>();
        expected.put("param1", "value1");
        expected.put("param2", "value2");
        expected.put("param3", "constant");

        Map<String, Object> result = parametersUtils.replace(input, documentContext);

        assertEquals(expected, result);
    }

    // Add more test cases covering typical use cases, edge cases, and error scenarios

}
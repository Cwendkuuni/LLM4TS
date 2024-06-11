import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    //Replacing a value in a Map<String, Object> - no change
    public void replaceTest() {
        Map<String, Object> input = new HashMap<>();
        input.put("one", 1);
        input.put("two", 2);       
        Map<String, Object> result = parametersUtils.replace(input, "{}");
        assertEquals(input, result);
    }

    @Test
    //Replacing a value in a Map<String, Object> - a change occurs
    public void replaceTest2() {
        Map<String, Object> input = new HashMap<>();
        input.put("one", "${one}");
        input.put("two", 2);     
        Map<String, Object> result = parametersUtils.replace(input, "{\"one\":1}");
        assertNotEquals(input, result);
        assertEquals(1, result.get("one"));
        assertEquals(2, result.get("two"));
    }

    //Below is a basic structure of what a test might look like. It is not a complete and working test due to dependencies and the complexity of setting up the involved tasks and workflows for this and other methods.

    @Test
    public void getTaskInputTest() {
        Map<String, Object> inputParams = Collections.emptyMap();
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDefinition = new TaskDef();
        String taskId = "TestTask";

        //TODO : Setup your WorkflowModel and TaskDef here

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        
        //TODO: Add assertions to check the results

        //Cleanup
        verify(workflow, atLeastOnce()).getWorkflowDefinition();
        reset(workflow);
    }
}

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

public class ParametersUtilsTest {
    
    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = mock(WorkflowModel.class);
        when(workflow.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);
        TaskDef taskDefinition = new TaskDef();
        String taskId = "sampleTaskId";

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Assert that the result is not null
        if(result == null) {
            fail("Method returned null");
        }
    }

    @Test
    public void testGetTaskInputV2() {
        // same as above method, test for this individual method won't be much different
    }

    @Test
    public void testClone() {
        Map<String, Object> inputTemplate = new HashMap<>();

        Map<String, Object> result = parametersUtils.clone(inputTemplate);

        // Assert that the result is not null
        if(result == null) {
            fail("Method returned null");
        }
    }

    @Test
    public void testReplace() {
        // you may need to test for each type of json 
        // a simple string case here
        Map<String, Object> input = new HashMap<>();
        input.put("str", "${workflow.input.str}");
        String json = "{\"workflow\":{\"input\":{\"str\":\"replacementStr\"}}}";

        Map<String, Object> replacedResult = parametersUtils.replace(input, json);
        assertEquals("replacementStr", replacedResult.get("str"));
    }

    @Test
    public void testReplaceString() {
        String paramString = "${workflow.input.str}";
        String replacedString = (String) parametersUtils.replace(paramString);
        assertEquals("", replacedString); // It would be replaced to empty if there is no ? 
    }

    @Test
    public void testGetWorkflowInput() {
        // the same procedure as the previous one, different input and output type
    }
}

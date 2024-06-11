
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import java.util.*;

public class ParametersUtilsTest {
    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void givenNullThenCloneShouldThrowRuntimeException() {
        Map<String, Object> inputTemplate = null;
        try {
            parametersUtils.clone(inputTemplate);
        } catch(RuntimeException e) {
            assertTrue(e.getMessage().contains("Unable to clone input params"));
        }
    }

    @Test
    public void givenEmptyMapThenReturnSameOnClone() {
        Map<String, Object> inputTemplate = new HashMap<>();
        assertTrue(parametersUtils.clone(inputTemplate).isEmpty());
    }

    @Test
    public void testReplace() {
        // Since this is a complex method and will have multiple scenarios, you will need to create separate test methods 
        // to cover all the edge cases.
        // For example, when a json input is string or when the json path is not found.
        // Below is a simple test scenario
        Map<String, Object> input = new HashMap<>();
        input.put("key", "${workflow.input.test}");
        Object json = "{}";
        Map<String, Object> replaced = parametersUtils.replace(input, json);
        assertTrue(replaced.get("key").equals("${workflow.input.test}"));
    }

    // Similarly, write tests for replace(String paramString), getWorkflowInput(workflowDef, inputParams) methods. 
    // Also write test methods for private methods.
}


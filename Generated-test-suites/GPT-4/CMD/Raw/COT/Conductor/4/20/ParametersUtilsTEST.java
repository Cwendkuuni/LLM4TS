import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;
    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(/* suppose we have had ObjectMapper instance here*/);
    }
    
    @Test
    public void testGetTaskInputWhenInputIsNull() {
        try {
            parametersUtils.getTaskInput(null, new WorkflowModel(), new TaskDef(), "taskId");
            fail("Expect exception to be thrown");
        } catch (RuntimeException re) {
            assertEquals("IllegalArgumentException expected", re.getMessage());
        }
    }
    
    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        // Add the specific values to the maps. you can add null values to test null condition 
        assertNotNull(parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef));
    }
    
    @Test
    public void testReplaceWhenInputIsNull() {
        Map<String, Object> input = null;
        try {
            parametersUtils.replace(input, "json");
            fail("Expect exception to be thrown");
        } catch (RuntimeException re) {
            assertEquals("IllegalArgumentException expected", re.getMessage());
        }
    }
    
    @Test
    public void testReplaceWhenJsonIsNull() {
        Map<String, Object> input = new HashMap<>();
        try {
            parametersUtils.replace(input, null);
            fail("Expect exception to be thrown");
        } catch (RuntimeException re) {
            assertEquals("IllegalStateException expected", re.getMessage());
        }
    }
    
    @Test
    public void testReplaceStringWhenParamIsNull() {
        try {
            parametersUtils.replace(null);
            fail("Expect exception to be thrown");
        } catch (RuntimeException re) {
            assertEquals("IllegalStateException expected", re.getMessage());
        }
    }
    
    @Test
    public void testGetWorkflowInputWhenWorkflowDefIsNull() {
        Map<String, Object> inputParams = new HashMap<>();
        try {
            parametersUtils.getWorkflowInput(null, inputParams);
            fail("Expect exception to be thrown");
        } catch (RuntimeException re) {
            assertEquals("IllegalArgumentException expected", re.getMessage());
        }
    }
}
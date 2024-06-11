
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class ParametersUtilsTest {
    private WorkflowDef workflowDef;
    private Map<String, Object> inputParams;
    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
        workflowDef = new WorkflowDef();
        inputParams = new HashMap<>();
        inputParams.put("key", "value");
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals("value", result.get("key"));
    }

    @After
    public void tearDown() {
        parametersUtils = null;
        workflowDef = null;
        inputParams = null;
    }
}

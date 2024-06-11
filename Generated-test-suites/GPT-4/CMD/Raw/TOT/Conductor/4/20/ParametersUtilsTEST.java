import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import java.util.Map;


class ParametersUtilsTest {

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowDef workflowDef = new WorkflowDef();
        // consider a inputParams map here.
        Map<String, Object> inputParams = Map.of("param1", "one", "param2", "two");
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        Assert.assertEquals(inputParams, workflowInput);
    }
}
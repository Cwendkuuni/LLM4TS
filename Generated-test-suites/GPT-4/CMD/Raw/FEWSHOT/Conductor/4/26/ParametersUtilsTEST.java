import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testReplace() {
        String expectedOutput = "sampleValue";

        Map<String, Object> maps = new HashMap<>();
        maps.put("sampleKey", "sampleValue");
        Map<String, Object> result = parametersUtils.replace(maps, "sampleValue");

        Assert.assertEquals(expectedOutput, result.get("sampleKey"));
    }

    @Test
    public void testGetWorkflowInput() {
    // Mocking WorkflowDef
    WorkflowDef workFlowDefinition = mock(WorkflowDef.class);
    when(workFlowDefinition.getInputTemplate()).thenReturn(null);

    // Calling GetWorkflowInput
    Map<String, Object> result = parametersUtils.getWorkflowInput(workFlowDefinition, new HashMap<String, Object>());
    
    // Asserting the result
    Assert.assertEquals(new HashMap<String, Object>(), result);
    }

    @Test
    public void replaceVariablesTest() {
        String expectedOutput = parametersUtils.replace("sampleKey").toString();
        Assert.assertEquals("", expectedOutput.trim());
    }

    @Test
    public void testGetTaskInputV1() {
    // Mock classes
    WorkflowModel workflowModel = mock(WorkflowModel.class);
    Map<String, Object> inputParams = new HashMap<>();

    // Perform the call
    Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);

    // Verify the result
    Assert.assertEquals(inputParams, result);
    }
}
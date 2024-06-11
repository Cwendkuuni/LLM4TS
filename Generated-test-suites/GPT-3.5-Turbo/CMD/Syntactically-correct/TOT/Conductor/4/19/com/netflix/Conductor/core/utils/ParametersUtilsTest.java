import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private WorkflowModel workflowModel;
    private TaskDef taskDef;
    private String taskId;
    private Map<String, Object> inputParams;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);

        workflowModel = new WorkflowModel();
        workflowModel.setInput(new HashMap<>());
        workflowModel.setOutput(new HashMap<>());
        workflowModel.setStatus("IN_PROGRESS");
        workflowModel.setWorkflowId("12345");

        taskDef = new TaskDef();

        // Mocking the EnvUtils
        EnvUtils envUtils = mock(EnvUtils.class);
        when(envUtils.getSystemParametersValue("testParam", taskId)).thenReturn("testParamValue");

        taskId = "task123";
        inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.param1");
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);
        assertEquals("Resulting map should not be null", result != null);
        assertEquals("param1 should have value 'workflow.input.param1'", result.get("param1"), "workflow.input.param1");
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals("Resulting map should not be null", result != null);
        assertEquals("param1 should have value 'workflow.input.param1'", result.get("param1"), "workflow.input.param1");
    }
}
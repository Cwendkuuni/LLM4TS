import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.setOutput(new HashMap<>());
        workflow.setStatus("COMPLETED");
        // Create a TaskDef with an inputTemplate
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, "taskId123", taskDefinition);
        assertEquals(0, taskInput.size());
    }

    @Test
    public void testReplaceVariables() {
        // Test replacing variables in a string
        String paramString = "${workflow.input.input1}";
        Object result = parametersUtils.replaceVariables(paramString, new HashMap<>(), "taskId123");
        assertEquals("", result);

    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(0, workflowInput.size());
    }
}
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.mockito.Mock;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ParametersUtils parametersUtils;

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        Map<String, Object> inputParams = Map.of("param1", "value1");
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "taskId";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Add assertions here
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        Map<String, Object> inputParams = Map.of("param1", "value1");
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "taskId";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

        // Add assertions here
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        Map<String, Object> input = Map.of("param1", "value1");
        Object json = "{\"key\": \"value\"}";

        Map<String, Object> replacedMap = parametersUtils.replace(input, json);

        // Add assertions here
    }

    @Test
    public void testReplaceVariables() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        String paramString = "${key}";
        Object json = "{\"key\": \"value\"}";

        Object replacedValue = parametersUtils.replaceVariables(paramString, json);

        // Add assertions here
    }

    // Add more test methods to cover all scenarios

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = Map.of("param1", "value1");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        // Add assertions here
    }

    // Add more test methods as needed

    
}
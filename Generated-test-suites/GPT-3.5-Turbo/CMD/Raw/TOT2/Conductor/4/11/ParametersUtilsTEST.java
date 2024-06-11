import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

    @Test
    public void testGetTaskInput() {
        // Typical Use Case
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task123";
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Assert statements for typical use case
    }

    @Test
    public void testGetTaskInputV2() {
        // Edge Case
        Map<String, Object> input = null;
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "task123";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        // Assert statements for edge case
    }

    @Test
    public void testReplace() {
        // Error Scenario
        String input = "";
        Object json = "";

        Object replaced = parametersUtils.replace(input, json);

        // Assert statements for error scenario
    }

    @Test
    public void testReplaceVariables() {
        // Edge Case
        String paramString = "";
        Object documentContext = "";
        String taskId = "task123";

        Object replacedVariables = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Assert statements for edge case
    }

    // Add more test cases for other methods in ParametersUtils class

}
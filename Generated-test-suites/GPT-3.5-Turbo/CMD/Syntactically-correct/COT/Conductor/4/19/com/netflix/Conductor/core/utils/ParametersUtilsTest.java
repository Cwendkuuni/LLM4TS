
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
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Assert statements to check the functionality of getTaskInput method
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "123";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        // Assert statements to check the functionality of getTaskInputV2 method
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = new Object();

        Map<String, Object> result = parametersUtils.replace(input, json);

        // Assert statements to check the functionality of replace method
    }

    @Test
    public void testreplaceVariables() {
        String paramString = "test";
        Object documentContext = new Object();
        String taskId = "123";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Assert statements to check the functionality of replaceVariables method
    }

    // Additional test cases for edge cases and exceptions can be added here

}

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Add assertions here to verify the functionality of getTaskInput method
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "task1";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        // Add assertions here to verify the functionality of getTaskInputV2 method
    }

    // Add more basic test cases for other public methods in ParametersUtils class

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        String jsonString = "{}";
        Object replacedObject = parametersUtils.replace(jsonString);

        // Add assertions here to verify the functionality of replace method
    }

    @Test
    public void testReplaceVariables() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());
        String paramString = "${test}";
        Object replacedValue = parametersUtils.replaceVariables(paramString, documentContext, "task1");

        // Add assertions here to verify the functionality of replaceVariables method
    }

    // Add more test cases for edge cases and exception handling scenarios

}
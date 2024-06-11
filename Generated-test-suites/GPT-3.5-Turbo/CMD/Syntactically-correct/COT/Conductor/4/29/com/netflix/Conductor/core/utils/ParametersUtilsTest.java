import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

        // Add assertions here
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "taskId";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        // Add assertions here
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        String json = "{}";

        Map<String, Object> result = parametersUtils.replace(input, json);

        // Add assertions here
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${param}";
        Object json = "{}";

        Object result = parametersUtils.replaceVariables(paramString, json);

        // Add assertions here
    }

    // Additional test cases for edge cases and exceptions handling scenarios can be added here

}

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

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        // Add assertions here to validate the result
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "task2";
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);
        // Add assertions here to validate the result
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = "{}";
        Object result = parametersUtils.replace(input, json);
        // Add assertions here to validate the result
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${param}";
        Object result = parametersUtils.replaceVariables(paramString, null, null, 0);
        // Add assertions here to validate the result
    }

    // Add more test methods for other public methods in ParametersUtils class
}


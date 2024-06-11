import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
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
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Add assertions here to test the functionality of getTaskInput method
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "task1";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        // Add assertions here to test the functionality of getTaskInputV2 method
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = "sample_json_string";

        Map<String, Object> result = parametersUtils.replace(input, json);

        // Add assertions here to test the functionality of replace method
    }

    // Add more test cases for other methods in ParametersUtils class

}
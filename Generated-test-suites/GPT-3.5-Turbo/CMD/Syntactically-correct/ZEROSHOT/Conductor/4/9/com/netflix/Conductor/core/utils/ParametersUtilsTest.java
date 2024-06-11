import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.HashMap;

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
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "123";
        Map<String, Object> inputParams = new HashMap<>();
        
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, taskId);
        
        // Add assertions here to test the taskInput map
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "123";
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        
        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDef);
        
        // Add assertions here to test the taskInputV2 map
    }

    @Test
    public void testReplace() {
        Map<String, Object> jsonMap = new HashMap<>();
        // Add appropriate JSON content to test replacement
        Object json = jsonMap;

        Map<String, Object> replacedMap = parametersUtils.replace(json);
        
        // Add assertions here to test the replacedMap
    }

    // Add more test methods for other functionalities of ParametersUtils class

}
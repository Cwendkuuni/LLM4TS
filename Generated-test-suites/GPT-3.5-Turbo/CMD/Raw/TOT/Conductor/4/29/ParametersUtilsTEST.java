import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        // Typical use case
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        
        // Ensure taskInput is not null
        assertEquals(1, taskInput.size());
    }

    @Test
    public void testGetTaskInputV2() {
        // Edge case
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "123";
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);
        
        // Ensure taskInputV2 is empty when input is null
        assertEquals(0, taskInputV2.size());
    }

    @Test
    public void testReplace() {
        // Error scenario
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());
        Map<String, Object> replaced = parametersUtils.replace(input, documentContext);
        
        // Ensure replaced is empty when input is empty
        assertEquals(0, replaced.size());
    }

}
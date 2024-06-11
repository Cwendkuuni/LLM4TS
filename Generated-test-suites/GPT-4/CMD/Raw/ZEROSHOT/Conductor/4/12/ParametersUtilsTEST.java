
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);
        assertEquals("value1", result.get("key1"));
    }
    
    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        String taskId = "1";
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
        input.put("key1", "value1");
        when(taskDefinition.getInputTemplate()).thenReturn(input);
        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflowModel, taskId, taskDefinition);
        assertEquals("value1", result.get("key1"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${value1}");
        Map<String, Object> result = parametersUtils.replace(input, "{}");
        assertEquals("${value1}", result.get("key1"));
    }

    @Test
    public void testReplaceList() {
        // This method is private in the source code. Therefore, we can't create an isolated test for this.
    }
    
    @Test
    public void testReplaceVariables() {
        // This method is private in the source code. Therefore, we can't create an isolated test for this.
    }
    
    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = Mockito.mock(WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        when(workflowDef.getInputTemplate()).thenReturn(inputParams);
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals("value1", result.get("key1"));
    }

}

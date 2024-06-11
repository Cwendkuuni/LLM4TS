import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "value2");

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals(2, result.size());
        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "task1";

        // Mock the behavior of getTaskInputV2 method
        when(objectMapper.writeValueAsBytes(inputParams)).thenReturn(new byte[]{});
        when(objectMapper.readValue(new byte[]{}, new TypeReference<Map<String, Object>>(){})).thenReturn(inputParams);
        when(objectMapper.writeValueAsBytes(taskDef.getInputTemplate())).thenReturn(new byte[]{});
        when(objectMapper.readValue(new byte[]{}, new TypeReference<Map<String, Object>>(){})).thenReturn(taskDef.getInputTemplate());

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDef, taskId);

        // Add assertions here based on the expected behavior of getTaskInput method
    }

    // Add more test methods to cover all scenarios

}
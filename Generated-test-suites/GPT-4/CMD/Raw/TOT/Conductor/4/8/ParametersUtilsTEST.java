
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInputV2() {
        // Create a mock ObjectMapper
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        // Initialize ParametersUtils
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        // Mock input params
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");

        // Mock WorkflowModel and TaskDef
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskDef taskDef = mock(TaskDef.class);

        // Mock TaskDef's inputTemplate
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("templateKey", "templateValue");
        when(taskDef.getInputTemplate()).thenReturn(inputTemplate);

        // Mock the outputs for WorkflowModel methods
        when(workflowModel.getWorkflowDefinition()).thenReturn(null);
        when(workflowModel.getWorkflowId()).thenReturn("wfId");
        // etc...

        // Test the getTaskInputV2 method
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);

        // Assert that the taskInput has expected values
        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("param1"));
        assertEquals("templateValue", taskInput.get("templateKey"));
    }


    // Include more test method here to validate edge cases and error scenarios.
}

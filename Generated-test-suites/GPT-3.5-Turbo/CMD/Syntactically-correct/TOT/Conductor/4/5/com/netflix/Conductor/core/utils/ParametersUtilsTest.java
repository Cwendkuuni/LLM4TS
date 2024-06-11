import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    @Mock
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskInputV1() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.param1");
        inputParams.put("param2", "workflow.input.param2");
        
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, null, null);
        
        assertEquals(2, result.size());
        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        
        // Setup example input for testing
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        
        // Mock behavior for taskDefinition.getInputTemplate()
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.getInputTemplate().put("key1", "value1");
        taskDefinition.getInputTemplate().put("key2", "value2");
        
        // Mock input for getTaskInputV2()
        Map<String, Object> input = new HashMap<>();
        input.put("input", "workflow.input.param1");
        
        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, null, taskDefinition);
        
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value1", result.get("input"));
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());
        workflowDef.getInputTemplate().put("key1", "value1");
        workflowDef.getInputTemplate().put("key2", "value2");
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        
        assertEquals(2, result.size());
        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("key2"));
    }

}
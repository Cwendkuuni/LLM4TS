
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.WorkflowModel;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {
    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("sampleKey", "sampleValue");
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(inputParams);
        String taskId = "sampleTaskId";
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(result);
        assertEquals("sampleValue", result.get("sampleKey"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("sampleKey", "sampleValue");
        workflowDef.setInputTemplate(inputParams);
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(result);
        assertEquals("sampleValue", result.get("sampleKey"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("sampleKey", "sampleValue");
        Object json = new Object();
        Map<String, Object> result = parametersUtils.replace(input, json);
        assertNotNull(result);
        assertEquals("sampleValue", result.get("sampleKey"));
    }

    @Test
    public void testReplaceWithJsonString() {
        Map<String, Object> input = new HashMap<>();
        input.put("sampleKey", "sampleValue");
        Object json = "{}";
        Map<String, Object> result = parametersUtils.replace(input, json);
        assertNotNull(result);
        assertEquals("sampleValue", result.get("sampleKey"));
    }

    @Test
    public void testReplaceMethodWithParamString() {
        String paramString = "sampleValue";
        Object result = parametersUtils.replace(paramString);
        assertEquals("sampleValue", result);
    }
}

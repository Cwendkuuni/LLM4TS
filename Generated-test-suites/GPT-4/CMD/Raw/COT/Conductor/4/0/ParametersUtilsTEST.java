
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private Map<String, Object> inputParms;
    private WorkflowModel workflow;
    private TaskDef taskDefinition;
    private String taskId = "testId";

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
        inputParms = new HashMap<>();
        inputParms.put("test", "value");

        // These instances (workflow, taskDefinition) should contain appropriate example values for your test
        workflow = new WorkflowModel();
        taskDefinition = new TaskDef();
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> result = parametersUtils.getTaskInput(inputParms, workflow, taskDefinition, taskId);
        assertNotNull(result);
        // Add assertions to compare result with expected values
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParms, workflow, taskId, taskDefinition);
        assertNotNull(result);
        // Add assertions to compare result with expected values
    }

    @Test
    public void testReplace() {
        String jsonString = "{\"key\":\"value\"}";
        Map<String, Object> result = parametersUtils.replace(inputParms, jsonString);
        assertNotNull(result);
        assertEquals(result.get("key"), "value");
    }

    @Test
    public void testReplaceString() {
        String paramString = "${key}";
        Object result = parametersUtils.replace(paramString);
        assertNotNull(result);
        // result should equal the replacement of ${key} with its value.
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(inputParms);
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParms);
        assertNotNull(result);
        assertEquals(result.get("test"), "value");
    }
}

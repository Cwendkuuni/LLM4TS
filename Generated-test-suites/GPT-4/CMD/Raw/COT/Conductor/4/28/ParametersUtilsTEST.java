
import org.junit.Test;
import org.junit.Before;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private Map<String, Object> inputParams;
    private WorkflowModel workflow;
    private TaskDef taskDefinition;
    private String taskId;

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        this.taskId = "123";
        this.inputParams = new HashMap<>();
        this.inputParams.put("keyTest", "valueTest");
        this.taskDefinition = new TaskDef();
        this.inputParams.put("keyTest", "valueTest");
        this.workflow = new WorkflowModel();
        this.parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> output = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(output);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> output = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
        assertNotNull(output);
    }

    @Test
    public void testReplace() {
        Map<String, Object> output = parametersUtils.replace(inputParams, taskId);
        assertNotNull(output);
    }

    @Test
    public void testReplaceString() {
        Object output = parametersUtils.replace("some String");
        assertNotNull(output);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> output = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(output);
    }

    // Test null conditions and exception handling
    @Test(expected = RuntimeException.class)
    public void testCloneWithException() {
        parametersUtils.clone(null);
    }

    @Test
    public void testGetTaskInputWithNullInput() {
        Map<String, Object> output = parametersUtils.getTaskInput(null, workflow, taskDefinition, taskId);
        assertNotNull(output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceWithException() {
        parametersUtils.replace(null, taskId);
    }

}

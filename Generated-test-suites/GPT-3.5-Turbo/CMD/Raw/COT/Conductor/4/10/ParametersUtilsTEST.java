
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel("workflowId", "workflowName");
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setName("task1");
        taskDefinition.getInputTemplate().put("key", "value");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

        assertNotNull(taskInput);
        assertEquals("value", taskInput.get("key"));
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel("workflowId", "workflowName");
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setName("task1");
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, "taskId", taskDefinition);

        assertNotNull(taskInput);
        assertEquals("value", taskInput.get("key"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "${workflow.input.value}");

        WorkflowModel workflow = new WorkflowModel("workflowId", "workflowName");
        workflow.getInput().put("value", "replaced");

        Map<String, Object> replacedMap = parametersUtils.replace(input, workflow);

        assertNotNull(replacedMap);
        assertEquals("replaced", replacedMap.get("key"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.value}";

        WorkflowModel workflow = new WorkflowModel("workflowId", "workflowName");
        workflow.getInput().put("value", "replaced");

        Object replacedValue = parametersUtils.replaceVariables(paramString, workflow, "taskId");

        assertNotNull(replacedValue);
        assertEquals("replaced", replacedValue);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.getInputTemplate().put("key", "value");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("anotherKey", "anotherValue");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals("value", workflowInput.get("key"));
        assertEquals("anotherValue", workflowInput.get("anotherKey"));
    }
}

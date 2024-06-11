import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(new HashMap<>());
        workflowModel.setOutput(new HashMap<>());
        workflowModel.setStatus("COMPLETED");
        workflowModel.setWorkflowId("12345");
        workflowModel.setParentWorkflowId("67890");
        workflowModel.setParentWorkflowTaskId("54321");
        workflowModel.setWorkflowName("TestWorkflow");
        workflowModel.setWorkflowVersion(1);
        workflowModel.setCorrelationId("corr123");
        workflowModel.setReasonForIncompletion(null);
        workflowModel.getWorkflowDefinition().setSchemaVersion(2);
        workflowModel.setVariables(new HashMap<>());
        workflowModel.setTasks(new ArrayList<>());

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflowModel, "98765", taskDef);

        assertNotNull(taskInput);
        assertEquals(8, taskInput.size());
        assertNotNull(taskInput.get("workflow"));
        assertNotNull(taskInput.get("userDefinedKey1"));
        assertNotNull(taskInput.get("userDefinedKey2"));
        assertNotNull(taskInput.get("userDefinedKey3"));
        assertNotNull(taskInput.get("userDefinedTaskName1"));
        assertNotNull(taskInput.get("userDefinedTaskName2"));
        assertNotNull(taskInput.get("userDefinedReferenceName1"));
        assertNotNull(taskInput.get("userDefinedReferenceName2"));
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals(3, workflowInput.size());
        assertNotNull(workflowInput.get("inputParam1"));
        assertNotNull(workflowInput.get("inputParam2"));
        assertNotNull(workflowInput.get("inputParam3"));
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${workflow.input.param1}");
        input.put("key2", "${task.output.param2}");

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("workflow", new WorkflowModel());
        jsonMap.put("task1", new TaskModel());
        jsonMap.put("task2", new TaskModel());

        Map<String, Object> replacedMap = parametersUtils.replace(input, jsonMap);

        assertNotNull(replacedMap);
        assertEquals(2, replacedMap.size());
        assertNotNull(replacedMap.get("key1"));
        assertNotNull(replacedMap.get("key2"));
        assertNotNull(replacedMap.get("key1"));
        assertNotNull(replacedMap.get("key2"));
    }

    @Test
    public void testReplaceString() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        String inputString = "${workflow.param1}";

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("param1", "value1");

        Object replacedValue = parametersUtils.replace(inputString);

        assertNotNull(replacedValue);
        assertEquals("value1", replacedValue);
    }

    @Test
    public void testReplaceVariables() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        DocumentContext documentContext = JsonPath.parse(new HashMap<>());

        String paramString = "${workflow.param1}";

        Object replacedValue = parametersUtils.replaceVariables(paramString, documentContext, "12345");

        assertNotNull(replacedValue);
        assertEquals("value1", replacedValue);
    }

}
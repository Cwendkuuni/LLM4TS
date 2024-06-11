
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputForSchemaVersion1() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.getWorkflowDefinition().setSchemaVersion(1);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.param1");
        inputParams.put("param2", "task1.output.param2");

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, null, "taskId");

        assertNotNull(taskInput);
        assertEquals("param1_value", taskInput.get("param1"));
        assertEquals("param2_value", taskInput.get("param2"));
    }

    @Test
    public void testGetTaskInputForSchemaVersion2() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.getWorkflowDefinition().setSchemaVersion(2);

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());
        taskDefinition.getInputTemplate().put("param3", "value3");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "workflow.input.param1");
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

        assertNotNull(taskInput);
        assertEquals("param1_value", taskInput.get("param1"));
        assertEquals("value3", taskInput.get("param3"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());
        workflowDef.getInputTemplate().put("param1", "value1");
        workflowDef.getInputTemplate().put("param2", "value2");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param3", "value3");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals("value1", workflowInput.get("param1"));
        assertEquals("value2", workflowInput.get("param2"));
        assertEquals("value3", workflowInput.get("param3"));
    }

}


package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.Map;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = Mockito.spy(new ObjectMapper());
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testClone() {
        Map<String, Object> inputTemplate = Collections.singletonMap("key", "value");
        Map<String, Object> cloned = parametersUtils.clone(inputTemplate);
        assertEquals(inputTemplate, cloned);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> input = Collections.singletonMap("key", "value");
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());

        parametersUtils.getTaskInput(input, workflowModel, null, "test");
        verify(objectMapper, atLeastOnce()).writeValueAsBytes(input);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = Collections.singletonMap("key", "value");
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());
        workflowModel.getTasks().add(new TaskModel());

        parametersUtils.getTaskInputV2(input, workflowModel, "test", new TaskDef());
        verify(objectMapper, atLeastOnce()).writeValueAsBytes(input);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = Collections.singletonMap("key", "${value}");
        Map<String, Object> replace = parametersUtils.replace(input, "{}");
        assertEquals("{key=${value}}", replace.toString());
    }

    @Test
    public void testReplaceString() {
        assertEquals("${value}", parametersUtils.replace("${value}").toString());
    }

    @Test
    public void testGetWorkflowInput() {
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("key", "value"));

        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("key2", "value2"));

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, taskDef.getInputTemplate());
        assertEquals("{key=value, key2=value2}", workflowInput.toString());
    }
}

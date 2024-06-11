
package com.netflix.conductor.core.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper mockObjectMapper;
    @Mock
    private WorkflowModel mockWorkflowModel;
    @Mock
    private WorkflowDef mockWorkflowDef;
    @Mock
    private TaskDef mockTaskDef;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(mockObjectMapper);
    }

    @Test
    public void getTaskInput_WhenSchemaVersionGreatThan1_ReturnsCorrectMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        when(mockWorkflowModel.getWorkflowDefinition()).thenReturn(mockWorkflowDef);
        when(mockWorkflowDef.getSchemaVersion()).thenReturn(2);
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, mockWorkflowModel, mockTaskDef, "task1");
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void getTaskInput_WhenSchemaVersionLessThan1_ReturnsCorrectMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        when(mockWorkflowModel.getWorkflowDefinition()).thenReturn(mockWorkflowDef);
        when(mockWorkflowDef.getSchemaVersion()).thenReturn(1);
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, mockWorkflowModel, mockTaskDef, "task1");
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void getWorkflowInput_WhenWorkflowDefNotNull_ReturnsCorrectMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("param2", "value2");
        when(mockWorkflowDef.getInputTemplate()).thenReturn(inputTemplate);
        Map<String, Object> result = parametersUtils.getWorkflowInput(mockWorkflowDef, inputParams);
        assertThat(result.size(), is(2));
    }

    @Test
    public void getWorkflowInput_WhenWorkflowDefNull_ReturnsCorrectMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        Map<String, Object> result = parametersUtils.getWorkflowInput(null, inputParams);
        assertThat(result.size(), is(1));
    }

    // TODO: Continue adding test cases for more coverage
}

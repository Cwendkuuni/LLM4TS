package com.netflix.conductor.core.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.PathNotFoundException;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapperMock = mock(ObjectMapper.class);

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(objectMapperMock);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskDef taskDefinition = mock(TaskDef.class);
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflowModel, "TaskId", taskDefinition);
        Assert.assertNotNull(taskInput);
    }

    @Test
    public void testReplaceMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param", "${workflow.input.param}");
        String json = "{ \"workflow\": { \"input\": { \"param\": \"value\" } } }";
        Map<String, Object> replaced = parametersUtils.replace(inputParams, json);
        Assert.assertEquals("value", replaced.get("param"));
    }

    @Test
    public void testReplaceString() {
        String input = "${workflow.input.param}";
        Object replaced = parametersUtils.replace(input);
        Assert.assertNotNull(replaced);
    }

    @Test(expected = PathNotFoundException.class)
    public void testReplaceString_Throws_PnfException() {
        String input = "${workflow.input.unknown_param}";
        Object replaced = parametersUtils.replace(input);
    }

    @Test
    public void testClone() throws IOException {
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> clonedMap = parametersUtils.clone(inputParams);
        Assert.assertEquals(inputParams, clonedMap);
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> map = new HashMap<>();
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        when(workflowDef.getInputTemplate()).thenReturn(map);
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, map);
        Assert.assertEquals(map, workflowInput);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskDef taskDefinition = new TaskDef();
        when(workflowModel.getWorkflowDefinition()).thenReturn(new WorkflowDef());

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "TaskId");
        Assert.assertNotNull(taskInput);
    }

    @Test(expected = RuntimeException.class)
    public void testClone_On_IOException() throws IOException {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        when(objectMapperMock.writeValueAsBytes(new HashMap<>())).thenThrow(IOException.class);
        parametersUtils = new ParametersUtils(objectMapperMock);
        parametersUtils.clone(new HashMap<>());
    }
}
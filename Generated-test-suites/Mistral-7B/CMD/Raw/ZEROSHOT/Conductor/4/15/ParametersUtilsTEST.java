package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private WorkflowModel workflowModel;
    @Mock
    private TaskDef taskDef;

    @Test
    public void testGetTaskInputV1() throws IOException {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "workflow value");
        inputParams.put("task.input.param2", "task value");

        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("workflow value", null);
        expectedOutput.put("task value", null);

        when(objectMapper.readValue(Mockito.anyByteArray(), Map.class)).thenAnswer(
                invocation -> invocation.getArgument(0));
        when(workflowModel.getInput()).thenReturn(Collections.singletonMap("param1", "workflow value"));

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);
        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetTaskInputV2() {
        // TODO: Implement test for getTaskInputV2 method
    }

    @Test
    public void testReplace() {
        String input = "${workflow.input.param1}.${task.input.param2}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$.workflow.input.param1")).thenReturn("workflow value");
        when(documentContext.read("$.task.input.param2")).thenReturn("task value");

        String expectedOutput = "workflow value.task value";
        String result = parametersUtils.replace(input, documentContext, null);
        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testReplaceVariables() {
        // TODO: Implement test for replaceVariables method
    }

    @Test
    public void testClone() throws IOException {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("param1", "inputTemplate value");

        byte[] bytes = Objects.requireNonNull(objectMapper).writeValueAsBytes(inputTemplate);
        Map<String, Object> expectedOutput = Objects.requireNonNull(objectMapper).readValue(bytes, Map.class);

        Map<String, Object> result = parametersUtils.clone(inputTemplate);
        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetWorkflowInput() {
        // TODO: Implement test for getWorkflowInput method
    }
}
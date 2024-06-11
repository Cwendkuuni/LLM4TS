
package com.netflix.conductor.core.utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ObjectMapper objectMapperMock;
    private ParametersUtils parametersUtilsUnderTest;

    @Before
    public void setUp() {
        objectMapperMock = Mockito.mock(ObjectMapper.class);
        parametersUtilsUnderTest = new ParametersUtils(objectMapperMock);
    }

    @Test
    public void testReplace() {
        Map<String, Object> inputMap = Collections.singletonMap("test_key", "${workflow.input.test_value}");
        DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("test_value", "20"));
        Map<String, Object> result = parametersUtilsUnderTest.replace(inputMap, documentContext);
        assertThat(result.get("test_key")).isEqualTo("20");
    }

    @Test
    public void testReplaceStringEnv() {
        System.setProperty("NAME", "TEST");
        String result = (String)parametersUtilsUnderTest.replace("${workflow.input.NAME}");
        assertThat(result).isEqualTo("TEST");
    }

    @Test
    public void testReplaceStringNoEnv() {
        String result = (String)parametersUtilsUnderTest.replace("${workflow.input.NAME}");
        assertThat(result).isEqualTo("${workflow.input.NAME}");
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("input_key", "test_input"));
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("input_extra", "extra");
        when(objectMapperMock.readValue(anyBytes(), any(TypeReference.class))).thenReturn(inputParams);
        final Map<String, Object> result = parametersUtilsUnderTest.getWorkflowInput(workflowDef, inputParams);

        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("input_key", "test_input");
        assertThat(result).containsEntry("input_extra", "extra");
    }
    
    @Test(expected = RuntimeException.class)
    public void testCloneIOException() throws IOException {
        Map<String, Object> inputParams = Collections.singletonMap("input_key", "test_input");
        when(objectMapperMock.writeValueAsBytes(inputParams)).thenThrow(IOException.class);
        parametersUtilsUnderTest.clone(inputParams);
    }
}

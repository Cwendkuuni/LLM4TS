import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private WorkflowModel workflowModel;
    private TaskDef taskDef;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = Mockito.mock(ObjectMapper.class);
        workflowModel = Mockito.mock(WorkflowModel.class);
        taskDef = Mockito.mock(TaskDef.class);
        when(objectMapper.writeValueAsBytes(Mockito.anyMap())).thenReturn(new byte[0]);
        when(objectMapper.readValue(Mockito.anyByteArray(), Mockito.any(TypeReference.class)))
                .thenAnswer(
                        invocation -> {
                            byte[] bytes = invocation.getArguments()[0];
                            TypeReference typeReference = invocation.getArguments()[1];
                            Map<String, Object> map = new HashMap<>();
                            map.put("testKey", "testValue");
                            return map;
                        });
    }

    @Test
    public void testGetTaskInput() throws IOException {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflowKey", "workflowKey");

        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowDef.Workflow workflow = new WorkflowDef.Workflow();
        workflow.setInput(Collections.singletonMap("testKey", "testValue"));
        workflowDef.setWorkflow(workflow);
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("testKey", "${workflow.input.testKey}"));

        String taskId = "taskId";

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);

        // Then
        Assert.assertEquals("testValue", result.get("testKey"));
    }

    @Test
    public void testReplaceVariables() {
        // Given
        String paramString = "${workflow.input.testKey}.${taskDef.inputTemplate.testKey}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        Assert.assertEquals("testValue.testValue", result.toString());
    }

    @Test
    public void testReplaceVariablesWithEmptyString() {
        // Given
        String paramString = "${}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        Assert.assertEquals("", result);
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariable() {
        // Given
        String paramString = "${env.TEST_VAR}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String envValue = "envValue";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        when(EnvUtils.getSystemParametersValue(paramString, null)).thenReturn(envValue);
        Assert.assertEquals(envValue, result);
    }

    @Test
    public void testReplaceVariablesWithInvalidPath() {
        // Given
        String paramString = "${workflow.input.testKey.$$}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        Assert.assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithNullValue() {
        // Given
        String paramString = "${workflow.input.nullKey}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        Assert.assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithComplexPath() {
        // Given
        String paramString = "${workflow.input.testKey.testKey2.testKey3}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read("testKey")).thenReturn("testValue");
        when(documentContext.read("testKey2"))
                .thenReturn(Collections.singletonMap("testKey3", "testValue3"));

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        Assert.assertEquals("testValue.testValue3", result.toString());
    }

    @Test
    public void testReplaceList() {
        // Given
        List<Object> list = new LinkedList<>();
        list.add("${workflow.input.testKey}");
        list.add("${taskDef.inputTemplate.testKey}");

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read("testKey")).thenReturn("testValue");
        when(documentContext.read("taskDef.inputTemplate.testKey")).thenReturn("testValue2");

        // When
        List<Object> result = parametersUtils.replaceList(list, null, documentContext);

        // Then
        Assert.assertEquals(List.of("testValue", "testValue2"), result);
    }

    @Test
    public void testReplaceVariablesWithComplexList() {
        // Given
        List<Object> list = new LinkedList<>();
        list.add(Map.of("testKey", "${workflow.input.testKey}"));
        list.add(Map.of("testKey2", "${taskDef.inputTemplate.testKey}"));

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read("testKey")).thenReturn("testValue");
        when(documentContext.read("taskDef.inputTemplate.testKey")).thenReturn("testValue2");

        // When
        List<Object> result = parametersUtils.replaceList(list, null, documentContext);

        // Then
        Assert.assertEquals(
                List.of(Map.of("testKey", "testValue"), Map.of("testKey2", "testValue2")), result);
    }

    @Test
    public void testReplaceVariablesWithComplexListAndEnvVariable() {
        // Given
        List<Object> list = new LinkedList<>();
        list.add(Map.of("testKey", "${env.TEST_VAR}"));
        list.add(Map.of("testKey2", "${workflow.input.testKey}"));

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String envValue = "envValue";
        when(EnvUtils.getSystemParametersValue("env.TEST_VAR", null)).thenReturn(envValue);
        when(documentContext.read("workflow.input.testKey")).thenReturn("testValue");

        // When
        List<Object> result = parametersUtils.replaceList(list, null, documentContext);

        // Then
        Assert.assertEquals(
                List.of(Map.of("testKey", envValue), Map.of("testKey2", "testValue")), result);
    }
}
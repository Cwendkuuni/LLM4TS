package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private static final Pattern PATTERN =
            Pattern.compile(
                    "(?=(?<!\\$)\\$\\{)(?:(?=.*?\\{(?!.*?\\1)(.*\\}(?!.*\\2).*))(?=.*?\\}(?!.*?\\2)(.*)).)+?.*?(?=\\1)[^{]*(?=\\2$)",
                    Pattern.DOTALL);

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;

    @BeforeEach
    public void setup() {
        this.objectMapper = Mockito.mock(ObjectMapper.class);
        this.parametersUtils = new ParametersUtils(objectMapper);
        this.workflowDef = Mockito.mock(WorkflowDef.class);
        this.taskDef = Mockito.mock(TaskDef.class);
        this.workflowModel = Mockito.mock(WorkflowModel.class);
    }

    @Test
    public void testGetTaskInput_ValidWorkflowAndTask_ReturnExpectedMap() throws IOException {
        // Given
        when(workflowDef.getSchemaVersion()).thenReturn(1L);
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel()));

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> assertThat(result, is(instanceOf(Map.class))),
                () -> Assertions.assertEquals(inputParams.get("testKey"), result.get("testKey"))
        );
    }

    @Test
    public void testGetTaskInput_NullInputParams_ReturnEmptyMap() {
        // Given
        when(workflowDef.getSchemaVersion()).thenReturn(1L);
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel()));

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, taskDef, "taskId");

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> assertThat(result, is(instanceOf(Map.class))),
                () -> Assertions.assertTrue(result.isEmpty())
        );
    }

    @Test
    public void testGetTaskInput_ValidWorkflowWithInputTemplate_ReturnExpectedMapWithTemplateValues()
            throws IOException {
        // Given
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("testKey", "testValueTemplate");

        when(workflowDef.getSchemaVersion()).thenReturn(1L);
        when(workflowDef.getInputTemplate()).thenReturn(inputTemplate);
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel()));

        Map<String, Object> inputParams = new HashMap<>();

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> assertThat(result, is(instanceOf(Map.class))),
                () -> Assertions.assertEquals(inputTemplate.get("testKey"), result.get("testKey"))
        );
    }

    @Test
    public void testGetTaskInput_ValidWorkflowWithInputTemplateAndCustomInputParams_ReturnExpectedMap()
            throws IOException {
        // Given
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("testKey", "testValueTemplate");

        when(workflowDef.getSchemaVersion()).thenReturn(1L);
        when(workflowDef.getInputTemplate()).thenReturn(inputTemplate);
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel()));

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testCustomValue");

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> assertThat(result, is(instanceOf(Map.class))),
                () -> Assertions.assertEquals(inputParams.get("testKey"), result.get("testKey"))
        );
    }

    @Test
    public void testReplaceVariables_ValidStringWithEnvironmentVariable_ReturnReplacedString() {
        // Given
        String paramString = "${TEST_ENV_VAR}";
        String taskId = "taskId";

        // When
        String result = parametersUtils.replaceVariables(paramString, null, taskId);

        // Then
        String sysValue = EnvUtils.getSystemParametersValue("TEST_ENV_VAR", taskId);
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> Assertions.assertEquals(sysValue, result)
        );
    }

    @Test
    public void testReplaceVariables_ValidStringWithJsonPath_ReturnReplacedString() {
        // Given
        String paramString = "${workflow.variables.testVar}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read("$.variables.testVar")).thenReturn("testValue");

        String taskId = "taskId";

        // When
        String result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> Assertions.assertEquals("testValue", result)
        );
    }

    @Test
    public void testReplaceVariables_ValidStringWithEmptyJsonPath_ReturnEmptyString() {
        // Given
        String paramString = "${}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        String taskId = "taskId";

        // When
        String result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> Assertions.assertEquals("", result)
        );
    }

    @Test
    public void testReplaceVariables_ValidStringWithNullJsonPath_ReturnOriginalString() {
        // Given
        String paramString = "${}";
        DocumentContext documentContext = null;

        String taskId = "taskId";

        // When
        String result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> Assertions.assertEquals("", result)
        );
    }

    @Test
    public void testReplaceVariables_ValidListWithEmptyList_ReturnOriginalList() {
        // Given
        List<String> list = Collections.emptyList();
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        // When
        List<String> result = parametersUtils.replaceList(list, "taskId", documentContext);

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> Assertions.assertEquals(list, result)
        );
    }

    @Test
    public void testReplaceVariables_ValidListWithString_ReturnReplacedList() {
        // Given
        List<String> list = Collections.singletonList("${workflow.variables.testVar}");
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read("$.variables.testVar")).thenReturn("testValue");

        // When
        List<String> result = parametersUtils.replaceList(list, "taskId", documentContext);

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> Assertions.assertEquals(Collections.singletonList("testValue"), result)
        );
    }

    @Test
    public void testReplaceVariables_ValidListWithNullJsonPath_ReturnOriginalList() {
        // Given
        List<String> list = Collections.singletonList("${}");
        DocumentContext documentContext = null;

        // When
        List<String> result = parametersUtils.replaceList(list, "taskId", documentContext);

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> Assertions.assertEquals(Collections.singletonList(""), result)
        );
    }

    @Test
    public void testReplaceVariables_ValidMapWithEmptyMap_ReturnOriginalMap() {
        // Given
        Map<String, Object> map = Collections.emptyMap();
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        // When
        Map<String, Object> result = parametersUtils.replace(map, documentContext, "taskId");

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> Assertions.assertEquals(map, result)
        );
    }

    @Test
    public void testReplaceVariables_ValidMapWithJsonPath_ReturnReplacedMap() {
        // Given
        Map<String, Object> map = new HashMap<>();
        map.put("testKey", "${workflow.variables.testVar}");
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read("$.variables.testVar")).thenReturn("testValue");

        // When
        Map<String, Object> result = parametersUtils.replace(map, documentContext, "taskId");

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> Assertions.assertEquals("testValue", result.get("testKey"))
        );
    }

    @Test
    public void testReplaceVariables_ValidMapWithNullJsonPath_ReturnOriginalMap() {
        // Given
        Map<String, Object> map = new HashMap<>();
        map.put("testKey", "${}");
        DocumentContext documentContext = null;

        // When
        Map<String, Object> result = parametersUtils.replace(map, documentContext, "taskId");

        // Then
        Assertions.assertAll(
                () -> assertThat(result, is(notNullValue())),
                () -> Assertions.assertEquals(map, result)
        );
    }

    private TaskModel taskModel() {
        return Mockito.mock(TaskModel.class);
    }
}
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.core.io.support.encoded.EncodedResourceProcessor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(anyString(), isA(TypeReference.class))).thenReturn(new HashMap<>());
    }

    @Test
    public void testGetTaskInput_whenTaskDefinitionIsNull_thenReturnEmptyMap() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "taskId";
        TaskDef taskDefinition = null;

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInput_whenInputParamsIsNull_thenReturnEmptyMap() {
        // Given
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "taskId";
        TaskDef taskDefinition = new TaskDef();

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(null, workflow, taskDefinition, taskId);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInput_whenInputTemplateIsPresent_thenMergeInputTemplateAndInputParams()
            throws IOException {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam1", "value1");
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "taskId";
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(Map.of("inputParam2", "value2"));

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Then
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("inputParam1", "value1");
        expectedResult.put("inputParam2", "value2");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInput_whenInputTemplateIsPresent_thenPriorityInputParamsOverInputTemplate()
            throws IOException {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam1", "value1");
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "taskId";
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(Map.of("inputParam1", "value2"));

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Then
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("inputParam1", "value1");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace_whenInputIsStringAndJsonPathIsValid_thenReplaceValueWithJsonPathValue()
            throws IOException {
        // Given
        String jsonString = "{\"inputParam1\": \"value1\", \"inputParam2\": \"${inputParam3}\"}";
        DocumentContext documentContext = createDocumentContext(jsonString);
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replace(jsonString, documentContext, taskId);

        // Then
        assertEquals("{\"inputParam1\": \"value1\", \"inputParam2\": \"value3\"}", result.toString());
    }

    @Test
    public void testReplace_whenInputIsStringAndJsonPathIsInvalid_thenReturnInputString() throws IOException {
        // Given
        String jsonString = "{\"inputParam1\": \"value1\", \"inputParam2\": \"${inputParam4}\"}";
        DocumentContext documentContext = createDocumentContext(jsonString);
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replace(jsonString, documentContext, taskId);

        // Then
        assertEquals(jsonString, result.toString());
    }

    @Test
    public void testReplace_whenInputIsMap_thenReplaceValueWithJsonPathValue() throws IOException {
        // Given
        String jsonString = "{\"inputParam1\": \"value1\", \"inputParam2\": {}}";
        DocumentContext documentContext = createDocumentContext(jsonString);
        String taskId = "taskId";

        // When
        Map<String, Object> result = (Map<String, Object>) parametersUtils.replace(jsonString, documentContext, taskId);

        // Then
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("inputParam1", "value1");
        expectedResult.put("inputParam2", "value3");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace_whenInputIsMapAndJsonPathIsInvalid_thenReturnInputMap() throws IOException {
        // Given
        String jsonString = "{\"inputParam1\": \"value1\", \"inputParam2\": {}}";
        DocumentContext documentContext = createDocumentContext(jsonString);
        String taskId = "taskId";

        // When
        Map<String, Object> result = (Map<String, Object>) parametersUtils.replace(jsonstring, documentContext, taskId);

        // Then
        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void testReplace_whenInputIsList_thenReplaceValueWithJsonPathValue() throws IOException {
        // Given
        String jsonString = "[{\"inputParam1\": \"value1\"}, {\"inputParam2\": \"${inputParam3}\"}]";
        DocumentContext documentContext = createDocumentContext(jsonstring);
        String taskId = "taskId";

        // When
        List result = (List) parametersUtils.replace(jsonstring, documentContext, taskId);

        // Then
        List expectedResult = List.of(
                Map.of("inputParam1", "value1"),
                Map.of("inputParam2", "value3")
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace_whenInputIsListAndJsonPathIsInvalid_thenReturnInputList() throws IOException {
        // Given
        String jsonstring = "[{\"inputParam1\": \"value1\"}, {\"inputParam2\": \"${inputParam4}\"}]";
        DocumentContext documentContext = createDocumentContext(jsonstring);
        String taskId = "taskId";

        // When
        List result = (List) parametersUtils.replace(jsonString, documentContext, taskId);

        // Then
        List expectedResult = List.of(
                Map.of("inputParam1", "value1"),
                Map.of("inputParam2", null)
        );
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplaceVariables_whenVariableIsEnvironment_thenReturnEnvironmentValue() {
        // Given
        String paramString = "${ENV_VAR}";
        DocumentContext documentContext = createEmptyDocumentContext();
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        String envValue = System.getenv("ENV_VAR");
        assertEquals(envValue, result);
    }

    @Test
    public void testReplaceVariables_whenVariableIsInvalid_thenReturnEmptyString() {
        // Given
        String paramString = "${INVALID_VAR}";
        DocumentContext documentContext = createEmptyDocumentContext();
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariables_whenVariableIsEmpty_thenReturnEmptyString() {
        // Given
        String paramString = "${}";
        DocumentContext documentContext = createEmptyDocumentContext();
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariables_whenVariableIsEmptyWithJsonPath_thenReturnEmptyString() {
        // Given
        String paramString = "${}.";
        DocumentContext documentContext = createEmptyDocumentContext();
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariables_whenVariableIsJsonPathWithInvalidPath_thenReturnEmptyString() {
        // Given
        String paramString = "$.invalidPath";
        DocumentContext documentContext = createEmptyDocumentContext();
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariables_whenVariableIsJsonPathWithMissingPath_thenReturnNull() {
        // Given
        String paramString = "$.";
        DocumentContext documentContext = createEmptyDocumentContext();
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetTaskInput_whenWorkflowDefinitionIsNull_thenReturnEmptyMap() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();

        // When
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, null, null, null);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInput_whenWorkflowDefinitionIsPresent_thenReturnInputMap() throws IOException {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "taskId";
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Map.of("inputParam1", "value1"));

        // When
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowDef, null, taskId);

        // Then
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("inputParam1", "value1");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInput_whenInputTemplateIsNull_thenReturnEmptyMap() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "taskId";
        WorkflowDef workflowDef = new WorkflowDef();

        // When
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowDef, null, taskId);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testReplaceList_whenInputIsEmptyList_thenReturnEmptyList() throws IOException {
        // Given
        String jsonString = "[]";
        DocumentContext documentContext = createDocumentContext(jsonString);
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceList(Collections.emptyList(), taskId, documentContext);

        // Then
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testReplaceList_whenInputIsListWithOneElement_thenReplaceValueWithJsonPathValue()
            throws IOException {
        // Given
        String jsonString = "[{ \"inputParam1\": \"value1\" }]";
        DocumentContext documentContext = createDocumentContext(jsonString);
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceList(List.of("value2"), taskId, documentContext);

        // Then
        List<Map<String, Object>> expectedResult = List.of(Map.of("inputParam1", "value2"));
        assertEquals(expectedResult, result);
    }

    private DocumentContext createDocumentContext(String jsonString) throws IOException {
        EncodedResource resource =
                new EncodedResource(getClass().getResource("/test-data.json"), "application/json");
        EncodedResourceProcessor processor = new EncodedResourceProcessor();
        InputStreamReader reader = new InputStreamReader(resource.getInputStream());
        Map<String, Object> jsonMap = objectMapper.readValue(reader, Map.class);
        when(objectMapper.readValue(jsonString, Map.class)).thenReturn(jsonMap);
        return JsonPath.parse(jsonString);
    }

    private DocumentContext createEmptyDocumentContext() {
        return JsonPath.parse(Collections.emptyMap());
    }
}
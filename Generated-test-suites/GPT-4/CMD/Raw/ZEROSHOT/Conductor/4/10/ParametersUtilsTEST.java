
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test(expected = RuntimeException.class)
    public void testCloneWithIOException() throws IOException {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key", "value");
        when(objectMapper.writeValueAsBytes(inputTemplate)).thenThrow(new IOException());

        parametersUtils.clone(inputTemplate);
    }

    @Test
    public void testClone() throws IOException {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key", "value");
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
        when(objectMapper.writeValueAsBytes(inputTemplate)).thenReturn(new byte[0]);
        when(objectMapper.readValue(new byte[0], typeReference)).thenReturn(inputTemplate);

        Map<String, Object> result = parametersUtils.clone(inputTemplate);

        assertEquals(inputTemplate, result);
    }

    @Test
    public void testReplaceWithJsonString() {
        String jsonString = "{}";
        Map<String, Object> input = Collections.singletonMap("key", "${value}");
        Map<String, Object> result = parametersUtils.replace(input, jsonString);
        assertNotNull(result);
    }
    
    @Test
    public void testReplaceWithJsonObj() {
        Map<String, Object> json = Collections.singletonMap("key", "value");
        Map<String, Object> input = Collections.singletonMap("key", "${value}");
        Map<String, Object> result = parametersUtils.replace(input, json);
        assertNotNull(result);
    }

    @Test
    public void testReplaceString() {
        String result = (String) parametersUtils.replace("${value}");
        assertNotNull(result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        Map<String, Object> inputParams = Collections.singletonMap("key", "value");
        Map<String, Object> inputTemplate = Collections.singletonMap("templateKey", "templateValue");
        when(workflowDef.getInputTemplate()).thenReturn(inputTemplate);
        when(objectMapper.writeValueAsBytes(inputTemplate)).thenReturn(new byte[0]);
        when(objectMapper.readValue(new byte[0], typeReference)).thenReturn(inputTemplate);
        
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("templateKey"));
        assertTrue(result.containsKey("key"));
    }
}

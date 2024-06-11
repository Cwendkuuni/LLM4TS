import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    public ParametersUtilsTest() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setName("TaskName");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "workflow.input.data");
        inputParams.put("key2", "${source.type.name}");

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, "taskId", taskDefinition);

        assertNotNull(result);
        assertEquals("workflow data value", result.get("key1"));
        assertNull(result.get("key2")); // Mock the document context to return null for this path
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "value1");
        input.put("key2", "${source.type.name}");

        Map<String, Object> json = new HashMap<>();
        json.put("source", Map.of("type", Map.of("name", "replacedValue")));

        Object result = parametersUtils.replace(input, json);

        assertNotNull(result);
        assertEquals("value1", ((Map)result).get("key1"));
        assertEquals("replacedValue", ((Map)result).get("key2"));
    }

    @Test
    public void testReplaceVariables() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "${source.type.name}");

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        Mockito.when(documentContext.read(Mockito.anyString())).thenReturn("resolvedValue");

        Object result = parametersUtils.replaceVariables("${key}", documentContext, "taskId");

        assertNotNull(result);
        assertEquals("resolvedValue", result);
    }
}

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(mock(ObjectMapper.class));
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<>());
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, "taskId");

        assertNotNull(taskInput);
        assertEquals(0, taskInput.size());
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<>());
        Map<String, Object> input = new HashMap<>();

        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflow, "taskId", taskDef);

        assertNotNull(taskInputV2);
        assertEquals(0, taskInputV2.size());
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        String json = "{\"key1\": \"value1\"}";

        Map<String, Object> replaced = parametersUtils.replace(input, json);

        assertNotNull(replaced);
        assertEquals("value1", replaced.get("key1"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${key1}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        Object replacedVariables = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertNotNull(replacedVariables);
        assertEquals("value1", replacedVariables);
    }
}

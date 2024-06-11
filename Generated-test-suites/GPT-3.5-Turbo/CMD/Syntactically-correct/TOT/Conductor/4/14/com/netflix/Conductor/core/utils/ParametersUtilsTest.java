import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        // Typical Use Case
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "task1";
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(1);

        Object result = parametersUtils.getTaskInput(new HashMap<>(), workflow, taskDef, taskId);
        // assert statements
    }

    @Test
    public void testGetTaskInputV2() {
        // Edge Case
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "task1";
        TaskDef taskDef = new TaskDef();

        Object result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDef);
        // assert statements
    }

    @Test
    public void testReplace() {
        // Error Scenario
        String json = "{\"key\": \"value\"}";

        Object result = parametersUtils.replace(json);
        // assert statements
    }

    @Test
    public void testReplaceVariables() {
        // Typical Use Case
        String paramString = "${key}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "task1";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        // assert statements
    }

    // Additional test cases for other methods in ParametersUtils class can be added here

}
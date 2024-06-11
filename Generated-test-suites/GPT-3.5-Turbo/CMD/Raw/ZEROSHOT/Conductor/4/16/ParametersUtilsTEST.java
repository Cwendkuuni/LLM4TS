import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "testTask";
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, taskId);
        // Add your assertions here
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "testTask";
        TaskDef taskDef = new TaskDef();
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDef);
        // Add your assertions here
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = new Object();
        Map<String, Object> replaced = parametersUtils.replace(input, json);
        // Add your assertions here
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "testParam";
        DocumentContext documentContext = new DocumentContext();
        String taskId = "testTask";
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        // Add your assertions here
    }

    // Add more test methods to cover all scenarios

}
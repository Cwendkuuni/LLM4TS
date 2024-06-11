
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ParametersUtilsTest {
    private static final ObjectMapper MOCK_MAPPER = mock(ObjectMapper.class);

    @Test
    public void testGetTaskInput() {
        ParametersUtils utils = new ParametersUtils(MOCK_MAPPER);
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";
        when(workflowModel.getWorkflowDefinition()).thenReturn(new WorkflowDef());
        Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, taskDefinition, taskId);
        assertNotNull(result);
    }

    @Test
    public void testClone() {
        ParametersUtils utils = new ParametersUtils(MOCK_MAPPER);
        Map<String, Object> inputTemplate = new HashMap<>();
        assertTrue(utils.clone(inputTemplate) instanceof Map);
    }

    @Test
    public void testReplace() {
        ParametersUtils utils = new ParametersUtils(MOCK_MAPPER);
        Map<String, Object> input = new HashMap<>();
        Configuration config = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext context = JsonPath.parse(new HashMap<String, Object>(), config);
        assertTrue(utils.replace(input, context) instanceof Map);
    }

    @Test
    public void testReplaceStringParam() {
        ParametersUtils utils = new ParametersUtils(MOCK_MAPPER);
        String paramString = "testParam";
        assertNotNull(utils.replace(paramString));
    }

    @Test
    public void testGetTaskInputV1() {
        ParametersUtils utils = new ParametersUtils(MOCK_MAPPER);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        Map<String, Object> inputParams = new HashMap<>();
        assertTrue(utils.getTaskInputV1(workflowModel, inputParams) instanceof Map);
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils utils = new ParametersUtils(MOCK_MAPPER);
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        assertTrue(utils.getWorkflowInput(workflowDef, inputParams) instanceof Map);
    }

}

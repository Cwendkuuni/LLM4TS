
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    private ParametersUtils paramsUtils;
    private ObjectMapper om;

    @Before
    public void setUp() {
        om = new ObjectMapper();
        paramsUtils = new ParametersUtils(om);
    }

    @Test
    public void testReplacement() {
        String str = paramsUtils.replace("${testParam}");
        assertNotNull(str);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef wd = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> inputs = paramsUtils.getWorkflowInput(wd, inputParams);
        assertNotNull(inputs);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowDefinition()).thenReturn(new WorkflowDef());
        TaskModel taskModel = mock(TaskModel.class);
        String taskId = "testId";
        Map<String, Object> inputParams = new HashMap<>();
        TaskDef taskDef = new TaskDef();

        Map<String, Object> inputs = paramsUtils.getTaskInput(inputParams, workflowModel, null, taskId);
        assertNotNull(inputs);

        inputs = paramsUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);
        assertNotNull(inputs);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReplace() {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> replaceInput = paramsUtils.replace(map, "{}");
        assertTrue(replaceInput.isEmpty());
    }
}

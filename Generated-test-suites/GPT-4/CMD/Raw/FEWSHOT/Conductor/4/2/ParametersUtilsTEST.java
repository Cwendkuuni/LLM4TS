
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        this.parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflowModel = mock(WorkflowModel.class);        
        TaskDef taskDef = mock(TaskDef.class);
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getSchemaVersion()).thenReturn(0);

        Map<String, Object> result = parametersUtils.getTaskInput(input, workflowModel, taskDef, "0");

        Assert.assertNotNull(result);
        verify(workflowDef, times(1)).getSchemaVersion();
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        Assert.assertEquals(inputParams, result);
    }

    @Test
    public void testReplace() {
        Map<String, Object> result = parametersUtils.replace(new HashMap<>(), Collections.emptyMap());

        Assert.assertNotNull(result);
    }

    @Test
    public void testReplaceString() {
        Object result = parametersUtils.replace("test");

        Assert.assertNotNull(result);
    }
}

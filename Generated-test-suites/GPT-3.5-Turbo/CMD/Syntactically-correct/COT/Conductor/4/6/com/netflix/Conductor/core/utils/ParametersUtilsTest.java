
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";
        assertNotNull(parametersUtils.getTaskInput(new HashMap<>(), workflow, taskDefinition, taskId));
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "123";
        TaskDef taskDefinition = new TaskDef();
        assertNotNull(parametersUtils.getTaskInputV2(new HashMap<>(), workflow, taskId, taskDefinition));
    }

    @Test
    public void testReplace() {
        assertEquals(1, parametersUtils.replace(new HashMap<>(), new Object()).size());
    }

    @Test
    public void testReplaceVariables() {
        assertEquals("", parametersUtils.replaceVariables("", null, null, 0));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        assertNotNull(parametersUtils.getWorkflowInput(workflowDef, new HashMap<>()));
    }
}

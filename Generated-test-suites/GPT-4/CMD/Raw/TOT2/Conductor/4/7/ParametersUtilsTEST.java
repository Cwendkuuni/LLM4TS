import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @Mock
    private ObjectMapper mockObjectMapper;

    @InjectMocks
    private ParametersUtils parametersUtils;

    private WorkflowModel workflowModel;
    private Map<String, Object> inputParams;

    @Before
    public void setUp() {
        workflowModel = mock(WorkflowModel.class);
        inputParams = new HashMap<String, Object>();
    }

    // A test case for schemaVersion > 1
    @Test
    public void testGetTaskInputV2() {
        when(workflowModel.getWorkflowDefinition()).thenReturn(new WorkflowDef());
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, "testTaskId");

        assertNotNull(result);
    }

    // A test case for schemaVersion <= 1
    @Test
    public void testGetTaskInputV1() {
        when(workflowModel.getWorkflowDefinition()).thenReturn(new WorkflowDef());
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(1);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, "testTaskId");

        assertNotNull(result);
    }
}
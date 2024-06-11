import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskDef taskDef;

    private String taskId = "task_id";

    private Map<String, Object> inputParams;
    private Map<String, Object> expectedOutput;

    @Before
    public void setUp() throws Exception {
        parametersUtils = new ParametersUtils(objectMapper);
        inputParams = new HashMap<>();
        expectedOutput = new HashMap<>();
    }

    @Test
    public void testGetTaskInput() {
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(1);
        when(parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId)).thenReturn(expectedOutput);
        Map<String, Object> actualOutput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);
        assertEquals(expectedOutput, actualOutput);
    }
}
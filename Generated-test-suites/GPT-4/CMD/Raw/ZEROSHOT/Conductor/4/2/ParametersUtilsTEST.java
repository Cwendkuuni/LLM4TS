
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDefinition = mock(TaskDef.class);
        assertEquals(Collections.emptyMap(), parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "id"));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDefinition = mock(TaskDef.class);
        assertEquals(Collections.emptyMap(), parametersUtils.getTaskInputV2(input, workflow, "id", taskDefinition));
    }

    @Test
    public void testClone() throws Exception {
        Map<String, Object> inputTemplate = new HashMap<>();
        when(objectMapper.writeValueAsBytes(inputTemplate)).thenReturn(new byte[0]);
        when(objectMapper.readValue(new byte[0], Map.class)).thenReturn(Collections.emptyMap());
        assertEquals(Collections.emptyMap(), parametersUtils.clone(inputTemplate));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        DocumentContext documentContext = JsonPath.newContext();
        assertEquals(Collections.emptyMap(), parametersUtils.replace(input, documentContext));
    }

    @Test
    public void testReplaceString() {
        assertEquals("", parametersUtils.replace(""));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        assertEquals(Collections.emptyMap(), parametersUtils.getWorkflowInput(workflowDef, inputParams));
    }
}

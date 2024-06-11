
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());
        workflowModel.getWorkflowDefinition().setSchemaVersion(1);

        assertEquals(inputParams, parametersUtils.getTaskInput(inputParams, workflowModel, new TaskDef(), ""));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());

        assertEquals(new HashMap<>(), parametersUtils.getTaskInputV2(inputParams, workflowModel, "", null));
    }

    @Test
    public void testClone() throws Exception {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("testKey", "testValue");

        when(objectMapper.writeValueAsBytes(inputTemplate)).thenReturn(new byte[0]);
        when(objectMapper.readValue(new byte[0], Map.class)).thenReturn(inputTemplate);

        assertEquals(inputTemplate, parametersUtils.clone(inputTemplate));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("testKey", "${testValue}");

        assertEquals(new HashMap<>(), parametersUtils.replace(input, Collections.emptyMap()));
    }

    @Test
    public void testReplaceString() {
        assertEquals("${testValue}", parametersUtils.replace("${testValue}"));
    }

    @Test
    public void testGetTaskInputV1() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.testKey", "testValue");

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(inputParams);

        assertEquals(new HashMap<>(), parametersUtils.getTaskInputV1(workflowModel, inputParams));
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(inputParams);

        assertEquals(inputParams, parametersUtils.getWorkflowInput(workflowDef, inputParams));
    }
}

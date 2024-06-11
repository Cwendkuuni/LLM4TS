
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    ParametersUtils parametersUtils;
    ObjectMapper mockedObjectMapper;
    
    @Before
    public void setUp() {
        mockedObjectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(mockedObjectMapper);
    }

    @Test
    public void testClone() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");
        inputMap.put("key2", "value2");

        try {
            when(mockedObjectMapper.writeValueAsBytes(inputMap)).thenReturn(new byte[0]);
            when(mockedObjectMapper.readValue(new byte[0], Map.class)).thenReturn(inputMap);
        } catch (Exception e) {

        }

        Map<String, Object> clonedMap = parametersUtils.clone(inputMap);

        assertEquals(inputMap, clonedMap);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel mockedWorkflowModel = mock(WorkflowModel.class);
        TaskDef mockedTaskDefinition = mock(TaskDef.class);

        when(mockedWorkflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(1);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams,mockedWorkflowModel, mockedTaskDefinition, "testTaskId");
        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void testGetTaskInputV2WithoutInput() {
        Map<String, Object> inputParams = null;
        WorkflowModel mockedWorkflowModel = mock(WorkflowModel.class);
        TaskDef mockedTaskDefinition = mock(TaskDef.class);

        when(mockedTaskDefinition.getInputTemplate()).thenReturn(null);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams,mockedWorkflowModel, "testTaskId", mockedTaskDefinition);
        assertEquals(new HashMap<>(), result);
    }

    // .... Other tests can follow the same structure and should cover all branches of tested methods.
}


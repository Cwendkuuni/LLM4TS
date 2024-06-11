import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ParametersUtilsTest {
    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;
    private Map<String, Object> map;
    private WorkflowModel workflowModel;
    private TaskDef taskDef;
    private String taskId;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
        map = new HashMap<>();
        workflowModel = new WorkflowModel(); //You need to set it up
        taskDef = new TaskDef(); //You need to set it up
        taskId = "sampleTaskId";
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> result = parametersUtils.getTaskInput(map, workflowModel, taskDef, taskId);
        // Assert statements and logic go here depending on what you want to test
        assertNotNull(result);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> result = parametersUtils.getTaskInputV2(map, workflowModel, taskId, taskDef);
        // Assert statements and logic go here
        assertNotNull(result);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = new Object();
        Map<String, Object> result = parametersUtils.replace(input, json);
        // Assert statements and logic go here
        assertNotNull(result);
    }

    @Test
    public void testReplaceParamString() {
        Object result = parametersUtils.replace("paramString");
        // Assert statements and logic go here
        assertNotNull(result);
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputParams = Collections.emptyMap();
        Map<String, Object> result = parametersUtils.getWorkflowInput(null, inputParams);
        // Assert statements and logic go here
        assertNotNull(result);
    }
}
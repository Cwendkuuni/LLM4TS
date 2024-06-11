
import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    ParametersUtils parametersUtils;
    Map<String, Object> testMap;
    WorkflowModel workflow;
    TaskDef taskDefinition;
    String taskId = "testTaskId";

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
        testMap = new HashMap<>();
        testMap.put("key1", "value1");
        testMap.put("key2", "value2");
        workflow = new WorkflowModel();
        taskDefinition = new TaskDef();
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> resultMap = parametersUtils.getTaskInput(testMap, workflow, taskDefinition, taskId);
        assertEquals(testMap.size(), resultMap.size());
        assertEquals(workflow, null);
        assertEquals(taskDefinition, null);
        assertEquals(taskId, "testTaskId");
    }

    @Test(expected = NullPointerException.class)
    public void testGetTaskInputWithNullInputs() {
        parametersUtils.getTaskInput(null, null, null, null);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> resultMap = parametersUtils.getTaskInputV2(testMap, workflow, taskId, taskDefinition);
        assertEquals(testMap.size(), resultMap.size());
        assertEquals(taskId, "testTaskId");
    }

    @Test(expected = NullPointerException.class)
    public void testGetTaskInputV2WithNullInputs() {
        parametersUtils.getTaskInputV2(null, null, null, null);
    }

    @Test
    public void testClone() {
        Map<String, Object> clonedMap = parametersUtils.clone(testMap);
        assertEquals(testMap.size(), clonedMap.size());
    }

    @Test(expected = RuntimeException.class)
    public void testCloneWithNullInputs() {
        parametersUtils.clone(null);
    }

    @Test
    public void testReplaceObject() {
        Map<String, Object> replacedMap = parametersUtils.replace(testMap, testMap);
        assertEquals(testMap.size(), replacedMap.size());
    }

    @Test(expected = NullPointerException.class)
    public void testReplaceObjectWithNullInputs() {
        parametersUtils.replace(null, null);
    }

    @Test
    public void testReplaceString() {
        Object replaced = parametersUtils.replace("key1");
        assertEquals("key1", replaced);
    }

    @Test(expected = NullPointerException.class)
    public void testReplaceStringWithNullInputs() {
        parametersUtils.replace(null);
    }

}

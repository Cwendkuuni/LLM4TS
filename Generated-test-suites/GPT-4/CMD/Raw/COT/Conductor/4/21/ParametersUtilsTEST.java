
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper mockObjectMapper;

    @Before
    public void setUp() {
        mockObjectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(mockObjectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> map = new HashMap<>();
        map.put("foo", "bar");

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowDefinition(new WorkflowDef());    

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setName("task1");

        Map<String, Object> result = parametersUtils.getTaskInput(map, workflow, taskDefinition, "task1");
        assertNotNull(result);
    }

    @Test
    public void testClone() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "value");

        Map<String, Object> result = parametersUtils.clone(input);
        assertEquals(input, result);
    }

    @Test(expected = RuntimeException.class)
    public void testCloneWithIOException() throws IOException {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "value");

        doThrow(new IOException()).when(mockObjectMapper).writeValueAsBytes(any());
        parametersUtils.clone(input);
    }

     @Test
     public void testReplace() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        Object json = new HashMap<>();

        Map<String, Object> result = parametersUtils.replace(map, json);
        
        assertNotNull(result);
    }

    @Test
    public void testReplaceWithString() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        String json = "{\"key\":\"value\"}";
        
        Map<String, Object> result = parametersUtils.replace(map, json);

        assertNotNull(result);
    }
    
    @Test
    public void testReplaceString() {
        String param = "key";

        Object result = parametersUtils.replace(param);

        assertNotNull(result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();

        HashMap<String, Object> map = new HashMap<>();
        map.put("key", "value");

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, map);

        assertNotNull(result);
    }
}

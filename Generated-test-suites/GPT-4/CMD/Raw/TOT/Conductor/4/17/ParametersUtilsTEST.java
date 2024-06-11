import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @After
    public void tearDown() {
        parametersUtils = null;
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "${value}");
        String json = "{\"value\" : \"replaced_value\"}";

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertNotNull(result);
        assertEquals("replaced_value", result.get("key"));
    }

    @Test
    public void testReplaceList() {
        // To be implemented in a similar fashion as testReplace
    }

    @Test
    public void testGetTaskInputV2() {
        // To be implemented. Ensure a complete task input map is returned.
    }

    @Test
    public void testGetTaskInput() {
        // To be implemented. Test both schema versions.
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        workflowDef.setInputTemplate(inputParams);

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals(inputParams, result);
    }

    @Test(expected = RuntimeException.class)
    public void testCloneIOException() throws IOException {
        ObjectMapper failingObjectMapper = new ExceptionThrowingObjectMapper();
        ParametersUtils uut = new ParametersUtils(failingObjectMapper);
        uut.clone(new HashMap<>());
    }

    /**
     * This ObjectMapper throws an IOException when attempting
     * to writeValueAsBytes, thereby simulating an IOException
     * in the clone method of the ParametersUtils class.
     */
    private static class ExceptionThrowingObjectMapper extends ObjectMapper {
        @Override
        public byte[] writeValueAsBytes(Object value) throws IOException {
            throw new IOException("Simulated exception");
        }
    }
}
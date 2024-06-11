import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtilsUnderTest;
    private ObjectMapper mockObjectMapper;

    @Before
    public void setUp() {
        mockObjectMapper = Mockito.mock(ObjectMapper.class);
        // The real instance of ParametersUtils should be created by passing mockObjectMapper
        parametersUtilsUnderTest = new ParametersUtils(mockObjectMapper);
    }

    @Test
    public void testGetTaskInputV1() {

        // Set up test case
        WorkflowModel mockWorkflow = Mockito.mock(WorkflowModel.class);
        when(mockWorkflow.getInput()).thenReturn(Collections.singletonMap("testKey", "testValue"));
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey1", "workflow.input.testKey");

        // Run the method under test
        Map<String, Object> result = parametersUtilsUnderTest.getTaskInputV1(mockWorkflow, inputParams);

        // Validate the result is as expected
        Assert.assertEquals("testValue", result.get("testKey1"));
    }
    
    // Rest of the test methods

}
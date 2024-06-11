import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput(){
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        ParametersUtils parametersUtils = new ParametersUtils(mockObjectMapper);
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Add assertions here
    }

    @Test
    public void testGetTaskInputV2(){
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        ParametersUtils parametersUtils = new ParametersUtils(mockObjectMapper);
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "123";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        // Add assertions here
    }

    @Test
    public void testReplace(){
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        ParametersUtils parametersUtils = new ParametersUtils(mockObjectMapper);
        Object json = new Object();

        Object result = parametersUtils.replace(json);

        // Add assertions here
    }

    @Test
    public void testReplaceVariables(){
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        ParametersUtils parametersUtils = new ParametersUtils(mockObjectMapper);
        String paramString = "test";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "123";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Add assertions here
    }

    // Additional test cases for other public methods should follow

}
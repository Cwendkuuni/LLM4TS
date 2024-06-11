import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Mock
    private ParametersUtils parametersUtils;

    private Map<String, Object> inputParams;
    private WorkflowModel workflowModel;
    private TaskDef taskDefinition;
    private String taskId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        inputParams = new HashMap<>();
        workflowModel = new WorkflowModel();
        taskDefinition = new TaskDef();
        taskId = "12345";
    }

    @Test
    public void testGetTaskInput() {
        // Test case for getTaskInput method
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, taskId);
        assertNotNull(taskInput);
        // Add more assertions as needed
    }

    @Test
    public void testGetTaskInputV2() {
        // Test case for getTaskInputV2 method
        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDefinition);
        assertNotNull(taskInputV2);
        // Add more assertions as needed
    }

    @Test
    public void testReplace() {
        // Test case for replace method
        Object replaced = parametersUtils.replace(inputParams, "{ \"key\": \"value\" }");
        assertNotNull(replaced);
        // Add more assertions as needed
    }

    @Test
    public void testReplaceVariables() {
        // Test case for replaceVariables method
        Object replacedVariables = parametersUtils.replaceVariables("${key}", JsonPath.parse("{ \"key\": \"value\" }"), "task123");
        assertNotNull(replacedVariables);
        // Add more assertions as needed
    }

    // Add more test cases for other methods in ParametersUtils class

}
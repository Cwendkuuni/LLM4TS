import org.junit.Test;
import static org.junit.Assert.*;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.workflow.WorkflowDef;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

 @Test
 public void testGetTaskInput_ValidInputParamsAndWorkflow() {
 WorkflowModel workflow = new WorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("inputParam1", "value1");
 WorkflowDef workflowDef = new WorkflowDef();
 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("workflowInput1", "workflowValue1");
 workflowDef.setInputTemplate(workflowInput);
 workflow.setWorkflowDefinition(workflowDef);

 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 Map<String, Object> result = utils.getTaskInput(inputParams, workflow, null, null);

 assertEquals("workflowValue1", result.get("workflowInput1"));
 assertEquals("value1", result.get("inputParam1"));
 }

 @Test
 public void testGetTaskInput_NullInputParamsAndWorkflow() {
 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 Map<String, Object> result = utils.getTaskInput(null, null, null, null);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetTaskInput_ValidInputParamsAndWorkflowWithNoInputTemplate() {
 WorkflowModel workflow = new WorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("inputParam1", "value1");
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setInputTemplate(null);
 workflow.setWorkflowDefinition(workflowDef);

 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 Map<String, Object> result = utils.getTaskInput(inputParams, workflow, null, null);

 assertEquals("value1", result.get("inputParam1"));
 }

 @Test(expected = RuntimeException.class)
 public void testGetTaskInput_NullWorkflowDefInputTemplate() {
 WorkflowModel workflow = new WorkflowModel();
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("inputParam1", "value1");
 WorkflowDef workflowDef = null;
 workflow.setWorkflowDefinition(workflowDef);

 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 Map<String, Object> result = utils.getTaskInput(inputParams, workflow, null, null);

 fail("Should throw RuntimeException");
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceVariables_NullJson() {
 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 String json = null;
 String taskId = "taskId";
 String paramString = "${workflow.workflowInput1}${workflow.workflowInput2}";

 Object replaced = utils.replaceVariables(paramString, null, taskId);

 fail("Should throw RuntimeException");
 }

 @Test
 public void testReplaceVariables_ValidJsonString() {
 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 String json = "{\"workflowInput1\": \"workflowValue1\", \"workflowInput2\": \"workflowValue2\"}";
 String taskId = "taskId";
 String paramString = "${workflow.workflowInput1}${workflow.workflowInput2}";

 Object replaced = utils.replaceVariables(paramString, JsonPath.parse(json), taskId);

 assertEquals("workflowValue1workflowValue2", replaced);
 }

 @Test(expected = RuntimeException.class)
 public void testReplaceVariables_InvalidJsonPath() {
 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 String json = "{\"workflowInput1\": \"workflowValue1\"}";
 String taskId = "taskId";
 String paramString = "${workflow.workflowInput2}"; // non-existent path

 Object replaced = utils.replaceVariables(paramString, JsonPath.parse(json), taskId);

 fail("Should throw RuntimeException");
 }

 @Test
 public void testReplaceVariables_ValidJsonNumberPath() {
 ParametersUtils utils = new ParametersUtils(new ObjectMapper());
 String json = "{\"workflowInput1\": 1, \"workflowInput2\": 2}";
 String taskId = "taskId";
 String paramString = "${workflow[1].workflowInput2}";

 Object replaced = utils.replaceVariables(paramString, JsonPath.parse(json), taskId);

 assertEquals(2, replaced);
 }
}
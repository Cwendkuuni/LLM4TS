import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testGetTaskInput() {
 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setInput(new HashMap<>());
 workflowDef.setInputTemplate(new HashMap<>());
 workflowModel.setWorkflowDefinition(workflowDef);

 TaskDef taskDef = new TaskDef();
 taskDef.setInputTemplate(new HashMap<>());

 Map<String, Object> inputParams = new HashMap<>();
 ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

 Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);
 assertNull(result);

 inputParams.put("workflow", workflowModel);
 result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);
 assertEquals(workflowModel.getInput(), result);
 }

 @Test
 public void testGetTaskInputV2() {
 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setSchemaVersion(2);
 workflowDef.setInput(new HashMap<>());
 workflowDef.setInputTemplate(new HashMap<>());
 workflowModel.setWorkflowDefinition(workflowDef);

 TaskDef taskDef = new TaskDef();
 Map<String, Object> inputTemplate = new HashMap<>();
 inputTemplate.put("${\\$['workflow.workflowId']}", "testWorkflowId");
 taskDef.setInputTemplate(inputTemplate);

 Map<String, Object> inputParams = new HashMap<>();
 ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

 Map<String, Object> result = parametersUtils.getTaskInputV2(
 inputParams, workflowModel, null, taskDef);
 assertEquals(workflowModel.getInput(), result);
 }

 @Test(expected = RuntimeException.class)
 public void testGetTaskInputWithIOException() {
 WorkflowModel workflowModel = new WorkflowModel();
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setSchemaVersion(2);
 workflowDef.setInput(new HashMap<>());
 workflowDef.setInputTemplate(new HashMap<>());
 workflowModel.setWorkflowDefinition(workflowDef);

 TaskDef taskDef = new TaskDef();
 Map<String, Object> inputTemplate = new HashMap<>();
 inputTemplate.put("${\\$['workflow.workflowId']}", "testWorkflowId");
 taskDef.setInputTemplate(inputTemplate);

 Map<String, Object> inputParams = new HashMap<>();
 ParametersUtils parametersUtils = spy(new ParametersUtils(objectMapper));
 doThrow(IOException.class).when(parametersUtils).writeValueAsBytes(anyMap());

 Map<String, Object> result = parametersUtils.getTaskInputV2(
 inputParams, workflowModel, null, taskDef);
 fail("Should have thrown IOException");
 }
}
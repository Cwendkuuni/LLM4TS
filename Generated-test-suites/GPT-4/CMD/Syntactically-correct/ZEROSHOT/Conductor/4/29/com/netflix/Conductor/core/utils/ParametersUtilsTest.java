package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

  @Mock
 ObjectMapper objectMapper;

  private ParametersUtils parametersUtils;

  @Before
  public void setup() throws IOException {
    MockitoAnnotations.initMocks(this);
    parametersUtils = new ParametersUtils(objectMapper);
    when(objectMapper.writeValueAsBytes(Collections.emptyMap())).thenReturn(new byte[0]);
    when(objectMapper.readValue(new byte[0], parametersUtils.map)).thenReturn(Collections.emptyMap());
  }

  @Test
  public void testReplace_String() {
    String replaced = (String) parametersUtils.replace("hello");
    assertEquals("hello", replaced);
  }
  
  @Test
  public void testReplace_Map() {
    Map<String, Object> input = new HashMap<>();
    input.put("key", "value");
    Map<String, Object> replaced = parametersUtils.replace(input, "json");
    assertEquals(input, replaced);
  }
  
  @Test
  public void testReplace_Variables() {
    String replaced = (String) parametersUtils.replace("test");
    assertEquals("test", replaced);
  }
  
  @Test
  public void testGetTaskInput() {
    Map<String, Object> inputParams = Collections.singletonMap("key", "value");
    WorkflowModel workflowModel = new WorkflowModel();
    WorkflowDef workflowDef = new WorkflowDef();
    TaskDef taskDef = new TaskDef();
    workflowModel.setWorkflowDefinition(workflowDef);
    TaskModel taskModel = new TaskModel();
    taskModel.setReferenceTaskName("TestRef");
    workflowModel.setTasks(Arrays.asList(taskModel));
    
    Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "1");
    assertEquals(inputParams, result);
  }
  
  @Test
  public void testGetWorkflowInput() {
    WorkflowDef workflowDef = new WorkflowDef();
    workflowDef.setInputTemplate(Collections.emptyMap());
    Map<String, Object> inputParams = Collections.singletonMap("key", "value");
    Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
    assertEquals(inputParams, result);
  }
}

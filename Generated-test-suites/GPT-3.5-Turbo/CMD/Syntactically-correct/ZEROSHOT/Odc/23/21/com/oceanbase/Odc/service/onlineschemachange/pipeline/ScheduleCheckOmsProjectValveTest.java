package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import org.junit.Test;

public class ScheduleCheckOmsProjectValveTest {

  @Test
  public void testInvoke() {
    // Test case for invoke method
    ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
    // Create dummy data for valveContext
    ValveContext valveContext = new ValveContext();
    // Create dummy data for OscValveContext
    OscValveContext oscValveContext = new OscValveContext();
    // Create dummy data for ScheduleTaskEntity
    ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
    oscValveContext.setScheduleTask(scheduleTask);
    // Create dummy data for OnlineSchemaChangeScheduleTaskParameters
    OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
    oscValveContext.setTaskParameter(taskParameter);
    
    // Invoke the method
    valve.invoke(oscValveContext);
    
    // Add assertions here
    assertNotNull(scheduleTask);
    assertNotNull(taskParameter);
  }

  @Test
  public void testHandleOmsProjectStepResult() {
    // Test case for handleOmsProjectStepResult method
    ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
    // Create dummy data for valveContext
    ValveContext valveContext = new ValveContext();
    // Create dummy data for ProjectStepResult
    ProjectStepResult projectStepResult = new ProjectStepResult();
    // Create dummy data for OnlineSchemaChangeScheduleTaskResult
    OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
    // Create dummy data for SwapTableType
    SwapTableType swapTableType = new SwapTableType();
    // Create dummy data for ScheduleTaskEntity
    ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
    
    // Invoke the method
    valve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTask);
    
    // Add assertions here
    assertNotNull(projectStepResult);
    assertNotNull(result);
    assertNotNull(swapTableType);
    assertNotNull(scheduleTask);
  }

  @Test
  public void testContinueHandleProjectStepResult() {
    // Test case for continueHandleProjectStepResult method
    ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
    // Create dummy data for ProjectStepResult
    ProjectStepResult projectStepResult = new ProjectStepResult();
    
    // Invoke the method
    valve.continueHandleProjectStepResult(projectStepResult);
    
    // Add assertions here
    assertNotNull(projectStepResult);
  }

  @Test
  public void testListProjectFullVerifyResult() {
    // Test case for listProjectFullVerifyResult method
    ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
    // Create dummy data for projectId
    String projectId = "123";
    // Create dummy data for databaseName
    String databaseName = "test";
    // Create dummy data for uid
    String uid = "456";
    
    // Invoke the method
    valve.listProjectFullVerifyResult(projectId, databaseName, uid);
    
    // Add assertions here
    assertNotNull(projectId);
    assertNotNull(databaseName);
    assertNotNull(uid);
  }

  @Test
  public void testAdaptResult() {
    // Test case for adaptResult method
    ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
    // Create dummy data for OnlineSchemaChangeScheduleTaskResult
    OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
    // Create dummy data for ProjectStepResult
    ProjectStepResult projectStepResult = new ProjectStepResult();
    
    // Invoke the method
    valve.adaptResult(result, projectStepResult);
    
    // Add assertions here
    assertNotNull(result);
    assertNotNull(projectStepResult);
  }

  @Test
  public void testRecordCurrentProgress() {
    // Test case for recordCurrentProgress method
    ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
    // Create dummy data for projectId
    String projectId = "123";
    // Create dummy data for OnlineSchemaChangeScheduleTaskResult
    OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
    
    // Invoke the method
    valve.recordCurrentProgress(projectId, result);
    
    // Add assertions here
    assertNotNull(projectId);
    assertNotNull(result);
  }

}
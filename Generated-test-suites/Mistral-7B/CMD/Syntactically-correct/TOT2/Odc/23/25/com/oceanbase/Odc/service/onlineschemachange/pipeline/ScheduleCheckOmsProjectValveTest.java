import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Component
public class ScheduleCheckOmsProjectValveTest {

 private ScheduleCheckOmsProjectValve valve;
 private ProjectOpenApiService projectOpenApiServiceMock;
 private OscTaskCompleteHandler completeHandlerMock;
 private ScheduleTaskRepository scheduleTaskRepositoryMock;
 private OnlineSchemaChangeProperties onlineSchemaChangePropertiesMock;

 @Autowired
 public void setUpMocks(ProjectOpenApiService projectOpenApiService,
         OscTaskCompleteHandler completeHandler, ScheduleTaskRepository scheduleTaskRepository,
         OnlineSchemaChangeProperties onlineSchemaChangeProperties) {
 this.projectOpenApiServiceMock = projectOpenApiServiceMock;
 this.completeHandlerMock = completeHandlerMock;
 this.scheduleTaskRepositoryMock = scheduleTaskRepositoryMock;
 this.onlineSchemaChangePropertiesMock = onlineSchemaChangePropertiesMock;

 valve = new ScheduleCheckOmsProjectValve();
 valve.setProjectOpenApiService(projectOpenApiServiceMock);
 valve.setOscTaskCompleteHandler(completeHandlerMock);
 valve.setScheduleTaskRepository(scheduleTaskRepositoryMock);
 valve.setOnlineSchemaChangeProperties(onlineSchemaChangePropertiesMock);
 }

 @Before
 public void initMocks() {
 Mockito.when(projectOpenApiServiceMock.describeProjectSteps(any()))
         .thenReturn(getTestProjectSteps());
 Mockito.when(projectOpenApiServiceMock.describeProjectProgress(any()))
         .thenReturn(getTestProjectProgress());
 Mockito.when(projectOpenApiServiceMock.resumeProject(any()))
         .thenReturn(null);
 Mockito.when(projectOpenApiServiceMock.listProjectFullVerifyResult(any()))
         .thenReturn(getTestProjectFullVerifyResultResponse());
 }

 @Test
 public void testInvokeWithSuccess() {
 ScheduleTaskEntity scheduleTask = createTestScheduleTask();
 OnlineSchemaChangeScheduleTaskParameters taskParameters = createTestTaskParameters();
 ValveContext context = createTestValveContext(scheduleTask, taskParameters);

 valve.invoke(context);

 assertEquals(TaskStatus.DONE, context.getScheduleTask().getProgressPercentage(), 0.1);
 verify(projectOpenApiServiceMock, times(1)).describeProjectSteps(any());
 verify(projectOpenApiServiceMock, times(1)).describeProjectProgress(any());
 verify(projectOpenApiServiceMock, times(1)).resumeProject(any());
 verify(projectOpenApiServiceMock, times(1)).listProjectFullVerifyResult(any());
 }

 @Test(expected = OscException.class)
 public void testInvokeWithError() {
 ScheduleTaskEntity scheduleTask = createTestScheduleTask();
 OnlineSchemaChangeScheduleTaskParameters taskParameters = createTestTaskParameters();
 ProjectStepResult projectStepResult = createTestProjectStepResult(PrecheckResult.FAILED);
 ProjectProgressResponse progressResponse = createTestProjectProgressResponse();
 ProjectFullVerifyResultResponse fullVerifyResultResponse = createTestProjectFullVerifyResultResponse();

 when(projectOpenApiServiceMock.describeProjectSteps(any()))
         .thenReturn(getTestProjectSteps());
 when(projectOpenApiServiceMock.describeProjectProgress(any()))
         .thenReturn(progressResponse);
 when(projectOpenApiServiceMock.listProjectFullVerifyResult(any()))
         .thenReturn(fullVerifyResultResponse);

 ValveContext context = createTestValveContext(scheduleTask, taskParameters);
 context.setProjectStepResult(projectStepResult);

 valve.invoke(context);

 assertEquals(TaskStatus.FAILED, context.getScheduleTask().getProgressPercentage(), 0.1);
 verify(projectOpenApiServiceMock, times(1)).describeProjectSteps(any());
 verify(projectOpenApiServiceMock, times(1)).describeProjectProgress(any());
 verify(projectOpenApiServiceMock, times(1)).listProjectFullVerifyResult(any());
 }

 // Add more test cases as needed for each method and edge cases

 private List<ProjectStepVO> getTestProjectSteps() {
 // Create test project steps and return them
 }

 private ProjectProgressResponse getTestProjectProgress() {
 // Create test project progress and return it
 }

 private ProjectFullVerifyResultResponse getTestProjectFullVerifyResultResponse() {
 // Create test project full verify result response and return it
 }

 private ScheduleTaskEntity createTestScheduleTask() {
 // Create test schedule task and return it
 }

 private OnlineSchemaChangeScheduleTaskParameters createTestTaskParameters() {
 // Create test online schema change schedule task parameters and return them
 }

 private ValveContext createTestValveContext(ScheduleTaskEntity scheduleTask,
 OnlineSchemaChangeScheduleTaskParameters taskParameters) {
 // Create test valve context and return it
 }

 private ProjectStepResult createTestProjectStepResult(PrecheckResult precheckResult) {
 // Create test project step result and return it
 }

 private ProjectProgressResponse createTestProjectProgressResponse() {
 // Create test project progress response and return it
 }

}
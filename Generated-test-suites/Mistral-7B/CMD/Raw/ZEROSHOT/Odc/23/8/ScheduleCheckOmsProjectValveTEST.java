package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

 private ProjectOpenApiService projectOpenApiService;
 private OscTaskCompleteHandler completeHandler;
 private ScheduleTaskRepository scheduleTaskRepository;
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
 private ScheduleCheckOmsProjectValve valveUnderTest;

 @Before
 public void setUp() {
 projectOpenApiService = Mockito.mock(ProjectOpenApiService.class);
 completeHandler = Mockito.mock(OscTaskCompleteHandler.class);
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 onlineSchemaChangeProperties = Mockito.mock(OnlineSchemaChangeProperties.class);
 valveUnderTest = new ScheduleCheckOmsProjectValve();
 valveUnderTest.setProjectOpenApiService(projectOpenApiService);
 valveUnderTest.setCompleteHandler(completeHandler);
 valveUnderTest.setScheduleTaskRepository(scheduleTaskRepository);
 valveUnderTest.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
 }

 @Test
 public void testInvoke_success() {
 // Given
 String projectId = "projectId";
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setOmsProjectId(projectId);
 taskParameter.setDatabaseName("databaseName");
 taskParameter.setUid("uid");
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(UUID.randomUUID().toString());
 scheduleTask.setStatus(TaskStatus.QUEUED.toString());
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 Mockito.when(scheduleTaskRepository.getById(projectId)).thenReturn(scheduleTask);
 List<ProjectStepVO> projectSteps = mockProjectSteps();
 ProjectProgressResponse progress = mockProjectProgressResponse();
 ProjectFullVerifyResultResponse fullVerifyResponse = mockProjectFullVerifyResponse();
 ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps, true)
 .withCheckerVerifyResult(() -> fullVerifyResponse)
 .withResumeProject(() -> {
 }).getCheckerResult();

 // When
 valveUnderTest.invoke(createValveContext(taskParameter, projectSteps, progress, fullVerifyResponse));

 // Then
 OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
 OnlineSchemaChangeScheduleTaskResult.class);
 assertEquals(TaskStatus.RUNNING.toString(), result.getStatus());
 assertSame(projectId, result.getOmsProjectId());
 assertSame("databaseName", result.getDatabaseName());
 assertSame("uid", result.getUid());
 assertTrue(result.isManualSwapTableEnabled());
 assertTrue(result.isManualSwapTableStarted());
 assertNull(result.getErrorMessage());
 }

 @Test
 public void testInvoke_OscException() {
 // Given
 String projectId = "projectId";
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setOmsProjectId(projectId);
 taskParameter.setDatabaseName("databaseName");
 taskParameter.setUid("uid");
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(UUID.randomUUID().toString());
 scheduleTask.setStatus(TaskStatus.QUEUED.toString());
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 Mockito.when(scheduleTaskRepository.getById(projectId)).thenReturn(scheduleTask);
 List<ProjectStepVO> projectSteps = mockProjectSteps();
 ProjectProgressResponse progress = mockProjectProgressResponse();
 ProjectFullVerifyResultResponse fullVerifyResponse = mockProjectFullVerifyResponseWithError();
 ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps, true)
 .withCheckerVerifyResult(() -> fullVerifyResponse)
 .withResumeProject(() -> {
 }).getCheckerResult();

 // When // Then
 try {
 valveUnderTest.invoke(createValveContext(taskParameter, projectSteps, progress, fullVerifyResponse));
 fail("Expected OscException");
 } catch (OscException e) {
 assertEquals(ErrorCodes.OmsPreCheckFailed, e.getCode());
 }

 // Verify
 OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
 OnlineSchemaChangeScheduleTaskResult.class);
 assertNull(result.getErrorMessage());
 assertFalse(result.isManualSwapTableEnabled());
 assertFalse(result.isManualSwapTableStarted());
 }

 @Test
 public void testInvoke_fullVerificationFailed() {
 // Given
 String projectId = "projectId";
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setOmsProjectId(projectId);
 taskParameter.setDatabaseName("databaseName");
 taskParameter.setUid("uid");
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(UUID.randomUUID().toString());
 scheduleTask.setStatus(TaskStatus.QUEUED.toString());
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 Mockito.when(scheduleTaskRepository.getById(projectId)).thenReturn(scheduleTask);
 List<ProjectStepVO> projectSteps = mockProjectSteps();
 ProjectProgressResponse progress = mockProjectProgressResponse();
 ProjectFullVerifyResultResponse fullVerifyResponse = mockProjectFullVerifyResponseWithError();
 ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps, true)
 .withCheckerVerifyResult(() -> fullVerifyResponse)
 .withResumeProject(() -> {
 }).getCheckerResult();

 // When
 valveUnderTest.invoke(createValveContext(taskParameter, projectSteps, progress, fullVerifyResponse));

 // Then
 OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
 OnlineSchemaChangeScheduleTaskResult.class);
 assertEquals(TaskStatus.FAILED.toString(), result.getStatus());
 assertSame(projectId, result.getOmsProjectId());
 assertSame("databaseName", result.getDatabaseName());
 assertSame("uid", result.getUid());
 assertFalse(result.isManualSwapTableEnabled());
 assertFalse(result.isManualSwapTableStarted());
 assertNull(result.getErrorMessage());
 }

 @Test
 public void testInvoke_fullVerificationSkipped() {
 // Given
 String projectId = "projectId";
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setOmsProjectId(projectId);
 taskParameter.setDatabaseName("databaseName");
 taskParameter.setUid("uid");
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(UUID.randomUUID().toString());
 scheduleTask.setStatus(TaskStatus.QUEUED.toString());
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 Mockito.when(scheduleTaskRepository.getById(projectId)).thenReturn(scheduleTask);
 List<ProjectStepVO> projectSteps = mockProjectSteps();
 ProjectProgressResponse progress = mockProjectProgressResponse();
 ProjectFullVerifyResultResponse fullVerifyResponse = mockProjectFullVerifyResponseWithSkipped();
 ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps, true)
 .withCheckerVerifyResult(() -> fullVerifyResponse)
 .withResumeProject(() -> {
 }).getCheckerResult();

 // When
 valveUnderTest.invoke(createValveContext(taskParameter, projectSteps, progress, fullVerifyResponse));

 // Then
 OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
 OnlineSchemaChangeScheduleTaskResult.class);
 assertEquals(TaskStatus.FAILED.toString(), result.getStatus());
 assertSame(projectId, result.getOmsProjectId());
 assertSame("databaseName", result.getDatabaseName());
 assertSame("uid", result.getUid());
 assertFalse(result.isManualSwapTableEnabled());
 assertFalse(result.isManualSwapTableStarted());
 assertNull(result.getErrorMessage());
 }

 private ProjectProgressResponse mockProjectProgressResponse() {
 ProjectProgressResponse response = Mockito.mock(ProjectProgressResponse.class);
 return response;
 }

 private ProjectFullVerifyResultResponse mockProjectFullVerifyResponse() {
 ProjectFullVerifyResultResponse response = Mockito.mock(ProjectFullVerifyResultResponse.class);
 return response;
 }

 private ProjectFullVerifyResultResponse mockProjectFullVerifyResponseWithError() {
 ProjectFullVerifyResultResponse response = Mockito.mock(ProjectFullVerifyResultResponse.class);
 Mockito.when(response.getStatus()).thenReturn("FAILED");
 Mockito.when(response.getResult().getStatus()).thenReturn("FAILED");
 return response;
 }

 private ProjectFullVerifyResultResponse mockProjectFullVerifyResponseWithSkipped() {
 ProjectFullVerifyResultResponse response = Mockito.mock(ProjectFullVerifyResultResponse.class);
 Mockito.when(response.getStatus()).thenReturn("SKIPPED");
 Mockito.when(response.getResult().getStatus()).thenReturn("SKIPPED");
 return response;
 }

 private List<ProjectStepVO> mockProjectSteps() {
 List<ProjectStepVO> steps = Lists.newArrayList();
 ProjectStepVO step1 = new ProjectStepVO();
 step1.setName("step1");
 step1.setStatus("RUNNING");
 steps.add(step1);
 ProjectStepVO step2 = new ProjectStepVO();
 step2.setName("step2");
 step2.setStatus("FAILED");
 steps.add(step2);
 return steps;
 }

 private ValveContext createValveContext(OnlineSchemaChangeScheduleTaskParameters taskParameter,
 List<ProjectStepVO> projectSteps, ProjectProgressResponse progress, ProjectFullVerifyResultResponse fullVerifyResponse) {
 OscValveContext context = new OscValveContext();
 context.setScheduleTask(new ScheduleTaskEntity());
 context.setTaskParameter(taskParameter);
 context.setProjectSteps(projectSteps);
 context.setProjectProgressResponse(progress);
 context.setProjectFullVerifyResponse(fullVerifyResponse);
 return context;
 }
}
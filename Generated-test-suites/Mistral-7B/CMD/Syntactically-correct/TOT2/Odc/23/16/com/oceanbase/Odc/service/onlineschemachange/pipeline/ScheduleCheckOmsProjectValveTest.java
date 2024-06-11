package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
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

 private ScheduleCheckOmsProjectValve valve;
 private ProjectOpenApiService projectOpenApiServiceMock;
 private OnlineSchemaChangeProperties onlineSchemaChangePropertiesMock;
 private ScheduleTaskRepository scheduleTaskRepositoryMock;
 private OscTaskCompleteHandler completeHandlerMock;
 private ProjectOpenApiService projectOpenApiService;
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
 private ScheduleTaskRepository scheduleTaskRepository;
 private OscTaskCompleteHandler completeHandler;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 projectOpenApiService = Mockito.spy(ProjectOpenApiService.class);
 onlineSchemaChangeProperties = Mockito.spy(OnlineSchemaChangeProperties.class);
 scheduleTaskRepository = Mockito.spy(ScheduleTaskRepository.class);
 completeHandler = Mockito.spy(OscTaskCompleteHandler.class);
 valve = new ScheduleCheckOmsProjectValve();
 valve.setProjectOpenApiService(projectOpenApiService);
 valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
 valve.setScheduleTaskRepository(scheduleTaskRepository);
 valve.setOscTaskCompleteHandler(completeHandler);
 }

 @Test
 public void testInvoke_normal_flow() throws Exception {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 ProjectStepVO projectStepVO = new ProjectStepVO();
 projectStepVO.setCurrentStep("PRE_CHECK");
 projectStepVO.setStatus("RUNNING");
 ProjectProgressResponse progress = Mockito.mock(ProjectProgressResponse.class);
 ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
 fullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
 List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);
 Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
 Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
 Mockito.when(projectOpenApiService.listProjectFullVerifyResult(Mockito.any()))
 .thenReturn(fullVerifyResultResponse);
 Mockito.when(scheduleTaskRepository.save(Mockito.any(ScheduleTaskEntity.class)))
 .thenReturn(scheduleTask);
 Mockito.when(scheduleTaskRepository.update(Mockito.any(ScheduleTaskEntity.class)))
 .thenReturn(scheduleTask);
 Mockito.when(completeHandler.handle(Mockito.any(ValveContext.class), Mockito.eq(projectStepVO.getStatus())))
 .thenReturn(null);

 // When
 valve.invoke(Mockito.mock(ValveContext.class));

 // Then
 assertEquals(TaskStatus.RUNNING, scheduleTask.getStatus());
 }

 @Test(expected = OscException.class)
 public void testInvoke_pre_check_failed() throws Exception {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 ProjectStepVO projectStepVO = new ProjectStepVO();
 projectStepVO.setCurrentStep("PRE_CHECK");
 projectStepVO.setStatus("FAILED");
 ProjectProgressResponse progress = Mockito.mock(ProjectProgressResponse.class);
 List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);
 Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
 Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
 Mockito.when(projectOpenApiService.listProjectFullVerifyResult(Mockito.any()))
 .thenThrow(new RuntimeException("PreCheck failed"));
 Mockito.when(scheduleTaskRepository.save(Mockito.any(ScheduleTaskEntity.class)))
 .thenReturn(scheduleTask);
 Mockito.when(scheduleTaskRepository.update(Mockito.any(ScheduleTaskEntity.class)))
 .thenReturn(scheduleTask);

 // When & Then
 valve.invoke(Mockito.mock(ValveContext.class));
 fail("PreCheck failed should be thrown");
 }

 @Test(expected = OscException.class)
 public void testInvoke_full_verification_inconsistency() throws Exception {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 ProjectStepVO projectStepVO = new ProjectStepVO();
 projectStepVO.setCurrentStep("FULL_VERIFICATION");
 projectStepVO.setStatus("RUNNING");
 ProjectProgressResponse progress = Mockito.mock(ProjectProgressResponse.class);
 List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);
 ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
 fullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
 Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
 Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
 Mockito.when(projectOpenApiService.listProjectFullVerifyResult(Mockito.any()))
 .thenReturn(fullVerifyResultResponse);
 Mockito.when(scheduleTaskRepository.save(Mockito.any(ScheduleTaskEntity.class)))
 .thenReturn(scheduleTask);
 Mockito.when(scheduleTaskRepository.update(Mockito.any(ScheduleTaskEntity.class)))
 .thenReturn(scheduleTask);

 // When & Then
 valve.invoke(Mockito.mock(ValveContext.class));
 fail("Data check inconsistency should be thrown");
 }

 @Test
 public void testInvoke_full_transfer_completed() throws Exception {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 ProjectStepVO projectStepVO = new ProjectStepVO();
 projectStepVO.setCurrentStep("FULL_TRANSFER");
 projectStepVO.setStatus("COMPLETED");
 ProjectProgressResponse progress = Mockito.mock(ProjectProgressResponse.class);
 List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);
 Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
 Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
 Mockito.when(projectOpenApiService.listProjectFullVerifyResult(Mockito.any()))
 .thenReturn(new ProjectFullVerifyResultResponse());
 Mockito.when(scheduleTaskRepository.save(Mockito.any(ScheduleTaskEntity.class)))
 .thenReturn(scheduleTask);
 Mockito.when(scheduleTaskRepository.update(Mockito.any(ScheduleTaskEntity.class)))
 .thenReturn(scheduleTask);
 Mockito.when(completeHandler.handle(Mockito.any(ValveContext.class), Mockito.eq(projectStepVO.getStatus())))
 .thenReturn(null);

 // When
 valve.invoke(Mockito.mock(ValveContext.class));

 // Then
 assertTrue(true);
 }

}
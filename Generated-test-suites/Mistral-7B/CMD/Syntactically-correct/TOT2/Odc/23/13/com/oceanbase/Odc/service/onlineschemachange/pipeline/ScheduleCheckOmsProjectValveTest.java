package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.constant.ErrorCodes;
import com.oceanbase.odc.common.exception.OscException;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OmsException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.OpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.BaseValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

 @Mock
 private OpenApiService projectOpenApiService;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
 @Mock
 private ProjectOpenApiService projectOpenApiServiceOpenApi;
 @Mock
 private ProjectStepResultChecker projectStepResultChecker;
 @Mock
 private SwapTableUtil swapTableUtil;
 @Mock
 private OscTaskCompleteHandler completeHandler;

 @InjectMocks
 private ScheduleCheckOmsProjectValve valveUnderTest;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testInvoke_NormalCase() throws Exception {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameters.setUid(UUID.randomUUID().toString());
 taskParameters.setOmsProjectId("test_project_id");
 taskParameters.setDatabaseName("test_database");
 taskParameters.setSwapTableType(SwapTableType.SWAP_TABLE_ON);

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId("test_schedule_task_id");
 scheduleTask.setStatus("SCHEDULED");
 scheduleTask.setResultJson("");

 when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
 .thenReturn(Lists.newArrayList(new ProjectStepVO("Step1", TaskStatus.RUNNING)));

 when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
 .thenReturn(new ProjectProgressResponse());

 when(projectOpenApiServiceOpenApi.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
 .thenReturn(new ProjectFullVerifyResultResponse());

 when(projectStepResultChecker.check(anyList(), anyList(), anyBoolean()))
 .thenReturn(new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT, null));

 when(swapTableUtil.isSwapTableEnable(SwapTableType.SWAP_TABLE_ON, "SCHEDULED", 100, FullVerificationResult.CONSISTENT, false))
 .thenReturn(true);

 when(completeHandler.handle(any(ValveContext.class), any(OnlineSchemaChangeScheduleTaskResult.class)))
 .thenReturn(true);

 when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class)))
 .thenReturn(true);

 when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);

 // When
 valveUnderTest.invoke(new ValveContext());

 // Then
 // Verify that the invoke method calls the expected methods in the correct order
 // and sets the correct values in the ScheduleTaskEntity
 }

 @Test(expected = OscException.class)
 public void testInvoke_PreCheckFailedCase() throws Exception {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameters.setUid(UUID.randomUUID().toString());
 taskParameters.setOmsProjectId("test_project_id");
 taskParameters.setDatabaseName("test_database");
 taskParameters.setSwapTableType(SwapTableType.SWAP_TABLE_ON);

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId("test_schedule_task_id");
 scheduleTask.setStatus("SCHEDULED");
 scheduleTask.setResultJson("");

 when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
 .thenReturn(Lists.newArrayList(new ProjectStepVO("Step1", TaskStatus.RUNNING)));

 when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
 .thenReturn(new ProjectProgressResponse());

 when(projectApiServiceOpenApi.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
 .thenThrow(new OmsException(ErrorCodes.OmsPreCheckFailed, "Precheck failed"));

 when(projectStepResultChecker.check(anyList(), anyList(), anyBoolean()))
 .thenReturn(new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT, null));

 when(swapTableUtil.isSwapTableEnable(SwapTableType.SWAP_TABLE_ON, "SCHEDULED", 100, FullVerificationResult.CONSISTENT, false))
 .thenReturn(true);

 when(completeHandler.handle(any(ValveContext.class), any(OnlineSchemaChangeScheduleTaskResult.class)))
 .thenReturn(true);

 when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class)))
 .thenReturn(true);

 when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);

 // When & Then
 valveUnderTest.invoke(new ValveContext());
 }

 @Test(expected = OscException.class)
 public void testInvoke_DataInconsistentCase() throws Exception {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameters.setUid(UUID.randomUUID().toString());
 taskParameters.setOmsProjectId("test_project_id");
 taskParameters.setDatabaseName("test_database");
 taskParameters.setSwapTableType(SwapTableType.SWAP_TABLE_ON);

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId("test_schedule_task_id");
 scheduleTask.setStatus("SCHEDULED");
 scheduleTask.setResultJson("");

 when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
 .thenReturn(Lists.newArrayList(new ProjectStepVO("Step1", TaskStatus.RUNNING)));

 when(projectApiService.describeProjectProgress(any(ProjectControlRequest.class)))
 .thenReturn(new ProjectProgressResponse());

 when(projectApiServiceOpenApi.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
 .thenReturn(new ProjectFullVerifyResultResponse());

 when(projectStepResultChecker.check(anyList(), anyList(), anyBoolean()))
 .thenReturn(new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.INCONSISTENT, "Test error message"));

 when(swapTableUtil.isSwapTableEnable(SwapTableType.SWAP_TABLE_ON, "SCHEDULED", 100, FullVerificationResult.INCONSISTENT, false))
 .thenReturn(true);

 when(completeHandler.handle(any(ValveContext.class), any(OnlineSchemaChangeScheduleTaskResult.class)))
 .thenReturn(true);

 when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class)))
 .thenReturn(true);

 when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);

 // When & Then
 valveUnderTest.invoke(new ValveContext());
 }
}
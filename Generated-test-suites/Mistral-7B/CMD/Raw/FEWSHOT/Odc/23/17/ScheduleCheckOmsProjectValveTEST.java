package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import com.google.common.collect.Lists;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

 @Mock
 private ProjectOpenApiService projectOpenApiService;
 @Mock
 private OscTaskCompleteHandler completeHandler;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
 @Mock
 private DefaultTableFactory tableFactory;

 @InjectMocks
 private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

 private ScheduleTaskEntity scheduleTask;
 private OnlineSchemaChangeScheduleTaskParameters taskParameter;
 private ProjectControlRequest projectRequest;
 private ProjectStepVO projectStepVO;
 private ProjectProgressResponse progress;
 private ProjectFullVerifyResultResponse fullVerifyResultResponse;
 private OnlineSchemaChangeScheduleTaskResult result;
 private OnlineSchemaChangeScheduleTaskResult lastResult;

 @Before
 public void setUp() {
 scheduleTask = new ScheduleTaskEntity();
 taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 projectRequest = new ProjectControlRequest();
 projectStepVO = new ProjectStepVO();
 progress = new ProjectProgressResponse();
 fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
 result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
 lastResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
 }

 @Test
 public void testInvokeWithSuccess() {
 // Given
 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
 ListProjectFullVerifyResultRequest fullVerifyRequest = mock(ListProjectFullVerifyResultRequest.class);
 when(projectOpenApiService.listProjectFullVerifyResult(fullVerifyRequest)).thenReturn(fullVerifyResultResponse);
 when(scheduleTaskRepository.update(scheduleTask)).thenReturn(scheduleTask);

 // When
 scheduleCheckOmsProjectValve.invoke(new ValveContextImpl());

 // Then
 verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
 verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
 verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(fullVerifyRequest);
 verify(scheduleTaskRepository, times(1)).update(scheduleTask);
 }

 @Test
 public void testInvokeWithError() {
 // Given
 doThrow(new RuntimeException("Error in describeProjectSteps")).when(projectOpenApiService).describeProjectSteps(projectRequest);
 doThrow(new RuntimeException("Error in describeProjectProgress")).when(projectOpenApiService).describeProjectProgress(projectRequest);
 doThrow(new RuntimeException("Error in listProjectFullVerifyResult")).when(projectOpenApiService).listProjectFullVerifyResult(fullVerifyRequest);
 doThrow(new RuntimeException("Error in updateScheduleTask")).when(scheduleTaskRepository).update(scheduleTask);

 // When
 assertThrows(RuntimeException.class, () -> scheduleCheckOmsProjectValve.invoke(new ValveContextImpl()));

 // Then
 verify(projectOpenApiService, times(3)).describeProjectSteps(projectRequest);
 verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
 verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(fullVerifyRequest);
 verify(scheduleTaskRepository, times(1)).update(scheduleTask);
 }

 @Test
 public void testHandleOmsProjectStepResultWithSuccessAndSwapTableEnabled() {
 // Given
 givenProjectStepResultWithDoneAndConsistent();
 givenSwapTableIsEnabled(true, 100, FullVerificationResult.CONSISTENT);

 // When
 scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new ValveContextImpl(), projectStepResult, result, SwapTableType.MANUAL, scheduleTask);

 // Then
 assertEquals(100, result.getFullVerificationProgressPercentage());
 assertEquals(TaskStatus.DONE, result.getCurrentStepStatus());
 assertTrue(result.isManualSwapTableEnabled());
 assertFalse(result.isManualSwapTableStarted());
 }

 @Test
 public void testHandleOmsProjectStepResultWithSuccessAndSwapTableDisabled() {
 // Given
 givenProjectStepResultWithDoneAndConsistent();
 givenSwapTableIsEnabled(false, 100, FullVerificationResult.CONSISTENT);

 // When
 scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new ValveContextImpl(), projectStepResult, result, SwapTableType.MANUAL, scheduleTask);

 // Then
 assertEquals(100, result.getFullVerificationProgressPercentage());
 assertEquals(TaskStatus.DONE, result.getCurrentStepStatus());
 assertFalse(result.isManualSwapTableEnabled());
 assertFalse(result.isManualSwapTableStarted());
 }

 @Test
 public void testHandleOmsProjectStepResultWithError() {
 // Given
 givenProjectStepResultWithFailedPrecheck();

 // When
 assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new ValveContextImpl(), projectStepResult, result, SwapTableType.MANUAL, scheduleTask));

 // Then
 assertEquals(ErrorCodes.OmsPreCheckFailed, result.getErrorMessage().getCode());
 }

 @Test
 public void testHandleOmsProjectStepResultWithInconsistentData() {
 // Given
 givenProjectStepResultWithFailedInconsistentData();

 // When
 assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new ValveContextImpl(), projectStepResult, result, SwapTableType.MANUAL, scheduleTask));

 // Then
 assertEquals(ErrorCodes.OmsDataCheckInconsistent, result.getErrorMessage().getCode());
 }

 private void givenProjectStepResultWithDoneAndConsistent() {
 projectStepResult = new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT, null);
 }

 private void givenProjectStepResultWithFailedPrecheck() {
 projectStepResult = new ProjectStepResult(TaskStatus.FAILED, PrecheckResult.FAILED, "Precheck failed");
 }

 private void givenProjectStepResultWithFailedInconsistentData() {
 projectStepResult = new ProjectStepResult(TaskStatus.FAILED, FullVerificationResult.INCONSISTENT, "Data inconsistent");
 }

 private void givenSwapTableIsEnabled(boolean isSwapTableEnabled, int progressPercentage, FullVerificationResult fullVerificationResult) {
 given(SwapTableUtil.isSwapTableEnable(SwapTableType.MANUAL, eq(scheduleTask.getStatus()), eq(result.getFullTransferProgressPercentage()),
 eq(result.getFullVerificationResult()), eq(result.isManualSwapTableStarted()))).thenReturn(isSwapTableEnabled);
 result.setFullVerificationResult(fullVerificationResult);
 result.setFullTransferProgressPercentage(progressPercentage);
 }
}
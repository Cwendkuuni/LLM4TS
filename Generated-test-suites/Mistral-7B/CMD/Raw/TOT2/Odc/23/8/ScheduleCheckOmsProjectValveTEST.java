import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule. ScheduleTaskRepository;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

public class ScheduleCheckOmsProjectValveTest {

 @MockBean
 private ProjectOpenApiService projectOpenApiService;
 @MockBean
 private OscTaskCompleteHandler completeHandler;
 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;
 @MockBean
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

 @Autowired
 private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

 @BeforeEach
 public void init() {
 ReflectionTestUtils.setFieldValue(scheduleCheckOmsProjectValve, "projectOpenApiService", projectOpenApiService);
 ReflectionTestUtils.setFieldValue(scheduleCheckOmsProjectValve, "completeHandler", completeHandler);
 ReflectionTestUtils.setFieldValue(scheduleCheckOmsProjectValve, "scheduleTaskRepository", scheduleTaskRepository);
 ReflectionTestUtils.setFieldValue(scheduleCheckOmsProjectValve, "onlineSchemaChangeProperties", onlineSchemaChangeProperties);
 }

 @Test
 public void testInvoke_Success() {
 // Given
 String projectId = UUID.randomUUID().toString();
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 projectRequest.setId(projectId);
 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(new ProjectStepVO()));
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setStatus("RUNNING");
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 when(scheduleTaskRepository.getOne(ArgumentMatchers.eq(1L))).thenReturn(scheduleTask);

 // When
 scheduleCheckOmsProjectValve.invoke(new ValveContext() {
 @Override
 public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
 return taskParameter;
 }
 });

 // Then
assertEquals(TaskStatus.RUNNING, ReflectionTestUtils.getFieldValue(scheduleTask, "status"));
 }

 @Test
 public void testInvoke_Failure() {
 // Given
 String projectId = UUID.randomUUID().toString();
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 projectRequest.setId(projectId);
 doThrow(new RuntimeException("Test Exception")).when(projectOpenApiService)
 .describeProjectSteps(projectRequest);

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setStatus("RUNNING");
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 when(scheduleTaskRepository.getOne(ArgumentMatchers.eq(1L))).thenReturn(scheduleTask);

 // When
 try {
 scheduleCheckOmsProjectValve.invoke(new ValveContext() {
 @Override
 public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
 return taskParameter;
 }
 });
 fail("Expect OscException");
 } catch (OscException e) {
 // Then
 assertEquals(ErrorCodes.TestException, e.getCode());
 }
 }

 @Test
 public void testInvoke_CurrentStep_PreCheck_Failed() {
 // Given
 String projectId = UUID.randomUUID().toString();
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 projectRequest.setId(projectId);
 ProjectStepVO projectStepVO = new ProjectStepVO();
 projectStepVO.setCurrentStep("PreCheck");
 projectStepVO.setCurrentStepStatus("FAILED");
 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setStatus("RUNNING");
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 when(scheduleTaskRepository.getOne(ArgumentMatchers.eq(1L))).thenReturn(scheduleTask);

 // When
 try {
 scheduleCheckOmsProjectValve.invoke(new ValveContext() {
 @Override
 public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
 return taskParameter;
 }
 });
 fail("Expect OscException");
 } catch (OscException e) {
 // Then
 assertEquals(ErrorCodes.OmsPreCheckFailed, e.getCode());
 }
 }

 @Test
 public void testInvoke_CurrentStep_FullVerification_Inconsistent() {
 // Given
 String projectId = UUID.randomUUID().toString();
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 projectRequest.setId(projectId);
 ProjectStepVO projectStepVO = new ProjectStepVO();
 projectStepVO.setCurrentStep("FullVerification");
 projectStepVO.setFullVerificationResult("INCONSISTENT");
 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setStatus("RUNNING");
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 when(scheduleTaskRepository.getOne(ArgumentMatchers.eq(1L))).thenReturn(scheduleTask);

 // When
 try {
 scheduleCheckOmsProjectValve.invoke(new ValveContext() {
 @Override
 public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
 return taskParameter;
 }
 });
 fail("Expect OscException");
 } catch (OscException e) {
 // Then
 assertEquals(ErrorCodes.OmsDataCheckInconsistent, e.getCode());
 }
 }

 @Test
 public void testInvoke_CurrentStep_FullVerification_Uncheck() {
 // Given
 String projectId = UUID.randomUUID().toString();
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 projectRequest.setId(projectId);
 ProjectStepVO projectStepVO = new ProjectStepVO();
 projectStepVO.setCurrentStep("FullVerification");
 projectStepVO.setFullVerificationResult("UNCHECK");
 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setStatus("RUNNING");
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 when(scheduleTaskRepository.getOne(ArgumentMatchers.eq(1L))).thenReturn(scheduleTask);

 // When
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(false);
 scheduleCheckOmsProjectValve.invoke(new ValveContext() {
 @Override
 public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
 return taskParameter;
 }
 });

 // Then
 assertTrue(result.isManualSwapTableEnabled());
 assertFalse(result.isManualSwapTableStarted());
 }

 @Test
 public void testInvoke_CurrentStep_FullVerification_Consistent() {
 // Given
 String projectId = UUID.randomUUID().toString();
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 projectRequest.setId(projectId);
 ProjectStepVO projectStepVO = new ProjectStepVO();
 projectStepVO.setCurrentStep("FullVerification");
 projectStepVO.setFullVerificationResult("CONSISTENT");
 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setStatus("RUNNING");
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 when(scheduleTaskRepository.getOne(ArgumentMatchers.eq(1L))).thenReturn(scheduleTask);

 // When
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 result.setManualSwapTableStarted(false);
 scheduleCheckOmsProjectValve.invoke(new ValveContext() {
 @Override
 public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
 return taskParameter;
 }
 });

 // Then
 assertNull(result.getManualSwapTableEnabled());
 assertNull(result.getManualSwapTableStarted());
 }
}
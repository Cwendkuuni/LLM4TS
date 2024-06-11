package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleCheckOmsProjectValveTest {

 private ProjectOpenApiService projectOpenApiService;
 private ProjectStepResultChecker projectStepResultChecker;
 private ScheduleTaskRepository scheduleTaskRepository;
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
 private ScheduleCheckOmsProjectValve valve;

 @Before
 public void setup() {
 projectOpenApiService = mock(ProjectOpenApiService.class);
 projectStepResultChecker = mock(ProjectStepResultChecker.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
 valve = new ScheduleCheckOmsProjectValve();
 valve.setProjectOpenApiService(projectOpenApiService);
 valve.setProjectStepResultChecker(projectStepResultChecker);
 valve.setScheduleTaskRepository(scheduleTaskRepository);
 valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
 }

 @Test
 public void testInvoke_success() throws OscException {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 projectRequest.setUid("uid");
 projectRequest.setId("omsProjectId");
 taskParameter.setProjectRequest(projectRequest);
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId("scheduleTaskId");
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(
 new ProjectStepVO()));
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());
 when(projectStepResultChecker.getCheckerResult(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
 .thenReturn(new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT, null));

 when(scheduleTaskRepository.update(ArgumentMatchers.any())).thenReturn(scheduleTask);

 // When
 valve.invoke(new OscValveContext(taskParameter, scheduleTask));

 // Then
 assertEquals(TaskStatus.DONE, valve.getCurrentStepStatus());
 }

 @Test(expected = OscException.class)
 public void testInvoke_preCheckFailed() throws OscException {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 projectRequest.setUid("uid");
 projectRequest.setId("omsProjectId");
 taskParameter.setProjectRequest(projectRequest);
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId("scheduleTaskId");
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(
 new ProjectStepVO()));
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());
 when(projectStepResultChecker.getCheckerResult(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
 .thenReturn(new ProjectStepResult(TaskStatus.FAILED, PrecheckResult.FAILED, "PreCheck Failed"));

 // When & Then
 valve.invoke(new OscValveContext(taskParameter, scheduleTask));
 assertEquals(ErrorCodes.OmsPreCheckFailed, valve.getErrorMsg());
 }

 @Test(expected = OscException.class)
 public void testInvoke_dataInconsistent() throws OscException {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ProjectControlRequest projectRequest = new ProjectControlRequest();
 projectRequest.setUid("uid");
 projectRequest.setId("omsProjectId");
 taskParameter.setProjectRequest(projectRequest);
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId("scheduleTaskId");
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(
 new ProjectStepVO()));
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());
 when(projectStepResultChecker.getCheckerResult(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
 .thenReturn(new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.INCONSISTENT, null));

 // When & Then
 valve.invoke(new OscValveContext(taskParameter, scheduleTask));
 assertEquals(ErrorCodes.OmsDataCheckInconsistent, valve.getErrorMsg());
 }
}
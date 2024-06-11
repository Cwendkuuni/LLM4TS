package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
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
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

 private ProjectOpenApiService projectOpenApiServiceMock;
 private OscTaskCompleteHandler completeHandlerMock;
 private ScheduleTaskRepository scheduleTaskRepositoryMock;
 private OnlineSchemaChangeProperties onlineSchemaChangePropertiesMock;
 private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

 @Before
 public void before() {
 projectOpenApiServiceMock = mock(ProjectOpenApiService.class);
 completeHandlerMock = mock(OscTaskCompleteHandler.class);
 scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);
 onlineSchemaChangePropertiesMock = mock(OnlineSchemaChangeProperties.class);

 scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
 scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiServiceMock);
 scheduleCheckOmsProjectValve.setCompleteHandler(completeHandlerMock);
 scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepositoryMock);
 scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangePropertiesMock);
 }

 @After
 public void after() {
 reset(projectOpenApiServiceMock, completeHandlerMock, scheduleTaskRepositoryMock, onlineSchemaChangePropertiesMock);
 }

 @Test
 public void testInvoke_success() throws Exception {
 // given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setUid(UUID.randomUUID().toString());
 taskParameter.setOmsProjectId("projectId");
 taskParameter.setDatabaseName("databaseName");
 taskParameter.setSwapTableType(SwapTableType.MANUAL);

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setStatus(TaskStatus.SUBMITTED.name());
 scheduleTask.setResultJson("{}");

 when(scheduleTaskRepositoryMock.findByUidAndOmsProjectId(taskParameter.getUid(), taskParameter.getOmsProjectId()))
 .thenReturn(scheduleTask);
 when(projectOpenApiServiceMock.describeProjectSteps(any()))
 .thenReturn(Lists.newArrayList(new ProjectStepVO()));
 when(projectOpenApiServiceMock.describeProjectProgress(any()))
 .thenReturn(new ProjectProgressResponse());
 when(projectOpenApiServiceMock.listProjectFullVerifyResult(any()))
 .thenReturn(new ProjectFullVerifyResultResponse());

 scheduleCheckOmsProjectValve.invoke(null);

 // then
 ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultArgumentCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
 verify(scheduleTaskRepositoryMock).update(resultArgumentCaptor.capture());
 OnlineSchemaChangeScheduleTaskResult result = resultArgumentCaptor.getValue();
 assertEquals(TaskStatus.SUCCEEDED.name(), result.getStatus());
 }

 @Test(expected = OscException.class)
 public void testInvoke_precheckFailed() throws Exception {
 // given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setUid(UUID.randomUUID().toString());
 taskParameter.setOmsProjectId("projectId");
 taskParameter.setDatabaseName("databaseName");
 taskParameter.setSwapTableType(SwapTableType.MANUAL);

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setStatus(TaskStatus.SUBMITTED.name());
 scheduleTask.setResultJson("{}");

 when(scheduleTaskRepositoryMock.findByUidAndOmsProjectId(taskParameter.getUid(), taskParameter.getOmsProjectId()))
 .thenReturn(scheduleTask);
 when(projectOpenApiServiceMock.describeProjectSteps(any()))
 .thenReturn(Lists.newArrayList(new ProjectStepVO()));
 when(projectOpenApiServiceMock.describeProjectProgress(any()))
 .thenReturn(new ProjectProgressResponse());
 when(projectOpenApiServiceMock.listProjectFullVerifyResult(any()))
 .thenThrow(new RuntimeException("Precheck failed"));

 scheduleCheckOmsProjectValve.invoke(null);
 }

 @Test(expected = OscException.class)
 public void testInvoke_dataInconsistent() throws Exception {
 // given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setUid(UUID.randomUUID().toString());
 taskParameter.setOmsProjectId("projectId");
 taskParameter.setDatabaseName("databaseName");
 taskParameter.setSwapTableType(SwapTableType.MANUAL);

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setStatus(TaskStatus.SUBMITTED.name());
 scheduleTask.setResultJson("{}");

 when(scheduleTaskRepositoryMock.findByUidAndOmsProjectId(taskParameter.getUid(), taskParameter.getOmsProjectId()))
 .thenReturn(scheduleTask);
 when(projectOpenApiServiceMock.describeProjectSteps(any()))
 .thenReturn(Lists.newArrayList(new ProjectStepVO()));
 when(projectOpenApiServiceMock.describeProjectProgress(any()))
 .thenReturn(new ProjectProgressResponse());
 when(projectOpenApiServiceMock.listProjectFullVerifyResult(any()))
 .thenReturn(new ProjectFullVerifyResultResponse());

 scheduleCheckOmsProjectValve.invoke(null);

 // then
 ArgumentCaptor<String> errorCodeArgumentCaptor = ArgumentCaptor.forClass(String.class);
 verify(projectOpenApiServiceMock, times(1)).listProjectFullVerifyResult(any());
 verify(projectOpenApiServiceMock, times(1)).resumeProject(any());
 assertEquals("Task failed for origin table has inconsistent data with new table, result: INCONSISTENT",
 errorCodeArgumentCaptor.capture().getValue());
 assertEquals(ErrorCodes.OmsDataCheckInconsistent.getCode(), errorCodeArgumentCaptor.getValue());
 }

 @Test
 public void testInvoke_projectNotExist() throws Exception {
 // given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setUid(UUID.randomUUID().toString());
 taskParameter.setOmsProjectId("projectId");
 taskParameter.setDatabaseName("databaseName");
 taskParameter.setSwapTableType(SwapTableType.MANUAL);

 when(scheduleTaskRepositoryMock.findByUidAndOmsProjectId(taskParameter.getUid(), taskParameter.getOmsProjectId()))
 .thenReturn(null);

 scheduleCheckOmsProjectValve.invoke(null);
 }
}
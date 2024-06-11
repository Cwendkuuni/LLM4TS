package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

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

 @Before
 public void setUp() {
 projectOpenApiService = mock(ProjectOpenApiService.class);
 completeHandler = mock(OscTaskCompleteHandler.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
 }

 @Mock
 private ProjectOpenApiService projectOpenApiService;
 @Mock
 private OscTaskCompleteHandler completeHandler;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

 @InjectMocks
 private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

 @Test
 public void testInvoke() throws Exception {
 // create test data
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setProjectId("testProjectId");
 taskParameter.setDatabaseName("testDatabaseName");
 taskParameter.setUid("testUid");
 taskParameter.setSwapTableType(SwapTableType.MANUAL);

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId("testScheduleTaskId");
 scheduleTask.setStatus(TaskStatus.SCHEDULED.toString());
 scheduleTask.setResultJson("");

 // mock behavior
 when(projectOpenApiService.describeProjectSteps(Mockito.any(ProjectControlRequest.class)))
 .thenReturn(Lists.newArrayList(new ProjectStepVO("1", "PreCheck", TaskStatus.RUNNING, null, null, null)));
 when(projectOpenApiService.describeProjectProgress(Mockito.any(ProjectControlRequest.class)))
 .thenReturn(new ProjectProgressResponse(100, List.of(new ProjectStepVO("1", "PreCheck", TaskStatus.RUNNING, null, null, null))));
 when(projectOpenApiService.listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class)))
 .thenReturn(new ProjectFullVerifyResultResponse(Lists.newArrayList(new ProjectFullVerifyResultResponse.ProjectFullVerifyResultItem("1", "testTableName", "FINISHED", "FINISHED", "Consistent"))));

 // execute test
 scheduleCheckOmsProjectValve.invoke(new ValveContext() {
 @Override
 public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
 return taskParameter;
 }

 @Override
 public ScheduleTaskEntity getScheduleTask() {
 return scheduleTask;
 }
 });

 // verify behavior
 assertEquals(TaskStatus.RUNNING, scheduleTask.getStatus());
 assertEquals(TaskStatus.RUNNING, scheduleTask.getLastStepStatus());
 }

 @Test(expected = OscException.class)
 public void testInvokeWithError() throws Exception {
 // create test data
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setProjectId("testProjectId");
 taskParameter.setDatabaseName("testDatabaseName");
 taskParameter.setUid("testUid");
 taskParameter.setSwapTableType(SwapTableType.MANUAL);

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId("testScheduleTaskId");
 scheduleTask.setStatus(TaskStatus.SCHEDULED.toString());
 scheduleTask.setResultJson("");

 // mock behavior with error
 when(projectOpenApiService.describeProjectSteps(Mockito.any(ProjectControlRequest.class)))
 .thenThrow(new RuntimeException("Test Exception"));

 // execute test
 try {
 scheduleCheckOmsProjectValve.invoke(new ValveContext() {
 @Override
 public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
 return taskParameter;
 }

 @Override
 public ScheduleTaskEntity getScheduleTask() {
 return scheduleTask;
 }
 });
 } catch (OscException e) {
 // verify behavior
 assertEquals(ErrorCodes.OmsDescribeProjectStepsFailed, e.getErrorCode());
 }

 // verify behavior
 assertEquals(TaskStatus.SCHEDULED, scheduleTask.getStatus());
 }
}
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
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

@SuppressWarnings("unchecked")
@InjectMocks
public class ScheduleCheckOmsProjectValveTest {

 private static final String OMS_PROJECT_ID = "12345";
 private static final String DATABASE_NAME = "test_db";
 private static final String UID = "test_uid";
 private static final int FULL_TRANSFER_ESTIMATED_COUNT = 100;
 private static final int FULL_TRANSFER_FINISHED_COUNT = 50;
 private static final int FULL_VERIFICATION_PROGRESS_PERCENTAGE = 75;
 private static final FullVerificationResult FULL_VERIFICATION_RESULT = FullVerificationResult.CONSISTENT;
 private static final PrecheckResult PRECHECK_RESULT = PrecheckResult.PASSED;
 private static final String FULL_VERIFICATION_RESULT_DESCRIPTION = "Full verification result description.";
 private static final String CURRENT_STEP = OmsStepName.FULL_VERIFIER.name();
 private static final String CURRENT_STEP_STATUS = TaskStatus.DONE.name();
 private static final String PROGRESS = String.valueOf(FULL_VERIFICATION_PROGRESS_PERCENTAGE);
 private static final String DESCRIPTION = "Full verification description.";

 @Mock
 private ProjectOpenApiService projectOpenApiService;
 @Mock
 private OscTaskCompleteHandler completeHandler;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

 private ScheduleCheckOmsProjectValve valve;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 valve = new ScheduleCheckOmsProjectValve();
 valve.setProjectOpenApiService(projectOpenApiService);
 valve.setCompleteHandler(completeHandler);
 valve.setScheduleTaskRepository(scheduleTaskRepository);
 valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
 }

 @Test
 public void testInvoke_whenProjectStepsReturned_thenHandleOmsProjectStepResultCalled() throws Exception {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
 taskParam.setOmsProjectId(OMS_PROJECT_ID);
 taskParam.setDatabaseName(DATABASE_NAME);
 taskParam.setUid(UID);
 ProjectControlRequest projectRequest = valve.getProjectRequest(taskParam);
 List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
 ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
 when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
 when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
 ProjectStepResult projectStepResult = new ProjectStepResult(progress, projectSteps,
 FULL_VERIFICATION_RESULT, null, FULL_VERIFICATION_RESULT_DESCRIPTION);
 ProjectFullVerifyResultResponse fullVerifyResult = mock(ProjectFullVerifyResultResponse.class);
 when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
 .thenReturn(fullVerifyResult);

 // When
 valve.invoke(mock(ValveContext.class));

 // Then
 ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
 verify(completeHandler).handle(eq(valve), resultCaptor.capture());
 OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
 assertEquals(OMS_PROJECT_ID, result.getProjectId());
 assertEquals(CURRENT_STEP, result.getCurrentStep());
 assertEquals(CURRENT_STEP_STATUS, result.getCurrentStepStatus());
 assertEquals(FULL_VERIFICATION_RESULT, result.getFullVerificationResult());
 assertEquals(FULL_VERIFICATION_RESULT_DESCRIPTION, result.getFullVerificationResultDescription());
 assertEquals(FULL_TRANSFER_ESTIMATED_COUNT, result.getFullTransferEstimatedCount());
 assertEquals(FULL_TRANSFER_FINISHED_COUNT, result.getFullTransferFinishedCount());
 assertEquals(PROGRESS, result.getFullTransferProgressPercentage());
 assertEquals(DESCRIPTION, result.getFullTransferDescription());
 }

 // Add more test cases for different scenarios

}
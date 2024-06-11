package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

@SuppressWarnings("unchecked")
@InjectMocks
public class ScheduleCheckOmsProjectValveTest {
    private static final int PROJECT_ID = 1;
    private static final String DATABASE_NAME = "testDB";
    private static final String UID = "testUid";
    private static final SwapTableType SWAP_TABLE_TYPE = SwapTableType.SWAP_TABLE_AFTER;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Mock
    private DefaultTableFactory defaultTableFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_ProjectFullTransferCompleted() throws OscException {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setOmsProjectId(PROJECT_ID);
        taskParam.setDatabaseName(DATABASE_NAME);
        taskParam.setUid(UID);
        taskParam.setSwapTableType(SWAP_TABLE_TYPE);

        ProjectControlRequest projectRequest = getProjectRequest(taskParam);
        ProjectStepVO projectStepVO1 = new ProjectStepVO();
        projectStepVO1.setStepName(OmsStepName.PRE_CHECK.name());
        projectStepVO1.setStepStatus(TaskStatus.DONE.name());
        projectStepVO1.setStepPercentage(100L);

        ProjectStepVO projectStepVO2 = new ProjectStepVO();
        projectStepVO2.setStepName(OmsStepName.FULL_TRANSFER.name());
        projectStepVO2.setStepStatus(TaskStatus.DONE.name());
        projectStepVO2.setStepPercentage(100L);

        ProjectStepVO projectStepVO3 = new ProjectStepVO();
        projectStepVO3.setStepName(OmsStepName.FULL_VERIFIER.name());
        projectStepVO3.setStepStatus(TaskStatus.DONE.name());
        projectStepVO3.setStepPercentage(100L);

        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO1, projectStepVO2, projectStepVO3);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProjectId(PROJECT_ID);
        progress.setProgressPercentage(100);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParam);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> listProjectFullVerifyResult(taskParam.getOmsProjectId(),
                                taskParam.getDatabaseName(), taskParam.getUid()))
                        .withResumeProject(() -> {
                            projectOpenApiService.resumeProject(projectRequest);
                            return null;
                        })
                        .getCheckerResult();

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        when(scheduleTaskRepository.update(resultCaptor.capture())).thenReturn(scheduleTaskRepository.findById(PROJECT_ID).get());

        // when
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.invoke(getValveContext(projectRequest, result));

        // then
        OnlineSchemaChangeScheduleTaskResult actualResult = resultCaptor.getValue();
        assertEquals(TaskStatus.DONE.name(), actualResult.getStatus());
        assertEquals(100, actualResult.getProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, actualResult.getFullVerificationResult());
    }

    @Test
    public void testInvoke_ProjectFullTransferFailed() throws OscException {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setOmsProjectId(PROJECT_ID);
        taskParam.setDatabaseName(DATABASE_NAME);
        taskParam.setUid(UID);
        taskParam.setSwapTableType(SWAP_TABLE_TYPE);

        ProjectControlRequest projectRequest = getProjectRequest(taskParam);
        ProjectStepVO projectStepVO1 = new ProjectStepVO();
        projectStepVO1.setStepName(OmsStepName.PRE_CHECK.name());
        projectStepVO1.setStepStatus(TaskStatus.DONE.name());
        projectStepVO1.setStepPercentage(100L);

        ProjectStepVO projectStepVO2 = new ProjectStepVO();
        projectStepVO2.setStepName(OmsStepName.FULL_TRANSFER.name());
        projectStepVO2.setStepStatus(TaskStatus.FAILED.name());
        projectStepVO2.setStepPercentage(0L);

        ProjectStepVO projectStepVO3 = new ProjectStepVO();
        projectStepVO3.setStepName(OmsStepName.FULL_VERIFIER.name());
        projectStepVO3.setStepStatus(TaskStatus.DONE.name());
        projectStepVO3.setStepPercentage(100L);

        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO1, projectStepVO2, projectStepVO3);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProjectId(PROJECT_ID);
        progress.setProgressPercentage(100);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParam);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> listProjectFullVerifyResult(taskParam.getOmsProjectId(),
                                taskParam.getDatabaseName(), taskParam.getUid()))
                        .withResumeProject(() -> {
                            projectOpenApiService.resumeProject(projectRequest);
                            return null;
                        })
                        .getCheckerResult();

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        when(scheduleTaskRepository.update(resultCaptor.capture())).thenReturn(scheduleTaskRepository.findById(PROJECT_ID).get());

        // when
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.invoke(getValveContext(projectRequest, result));

        // then
        OnlineSchemaChangeScheduleTaskResult actualResult = resultCaptor.getValue();
        assertEquals(TaskStatus.FAILED.name(), actualResult.getStatus());
        assertEquals(0, actualResult.getProgressPercentage());
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }

    private ValveContext getValveContext(ProjectControlRequest projectRequest, OnlineSchemaChangeScheduleTaskResult result) {
        OscValveContext valveContext = mock(OscValveContext.class);
        when(valveContext.getScheduleTask()).thenReturn(new ScheduleTaskEntity());
        when(valveContext.getTaskParameter()).thenReturn(taskParam);
        when(valveContext.getTaskParameter().getSwapTableType()).thenReturn(SWAP_TABLE_TYPE);
        when(valveContext.getProjectOpenApiService()).thenReturn(projectOpenApiService);
        when(valveContext.getScheduleTaskRepository()).thenReturn(scheduleTaskRepository);
        when(valveContext.getOscTaskCompleteHandler()).thenReturn(completeHandler);
        when(valveContext.getOnlineSchemaChangeProperties()).thenReturn(onlineSchemaChangeProperties);
        when(valveContext.getDefaultTableFactory()).thenReturn(defaultTableFactory);

        when(valveContext.getScheduleTask()).thenReturn(new ScheduleTaskEntity());
        when(valveContext.getScheduleTask().getId()).thenReturn(PROJECT_ID);
        when(valveContext.getScheduleTask().getResultJson()).thenReturn(JsonUtils.toJson(result));

        return valveContext;
    }

    private ProjectFullVerifyResultResponse listProjectFullVerifyResult(String projectId, String databaseName,
            String uid) {
        ProjectFullVerifyResultResponse response = mock(ProjectFullVerifyResultResponse.class);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(new ProjectStepVO(), new ProjectStepVO());
        when(projectOpenApiService.listProjectFullVerifyResult(new ListProjectFullVerifyResultRequest()))
                .thenReturn(response);
        when(projectOpenApiService.listProjectFullVerifyResult(new ListProjectFullVerifyResultRequest().setProjectId(projectId)
                .setDatabaseName(databaseName).setUid(uid))).thenReturn(response);
        return response;
    }
}
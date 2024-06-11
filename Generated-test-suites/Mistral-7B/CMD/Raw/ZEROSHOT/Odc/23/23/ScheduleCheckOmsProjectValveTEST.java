package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
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
import com.oceanbase.odc.service.onlineschemachange.utils.TestUtils;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void invokeTest() {
        UUID projectId = UUID.randomUUID();
        String databaseName = "testDatabase";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId.toString());
        taskParameter.setDatabaseName(databaseName);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepVO stepVO = mock(ProjectStepVO.class);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(stepVO));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(progress);
        when(stepVO.getCurrentStep()).thenReturn("FULL_TRANSFER");
        when(stepVO.getStatus()).thenReturn(TaskStatus.RUNNING.name());
        when(progress.getCurrentStepStatus()).thenReturn(TaskStatus.RUNNING.name());

        ProjectStepResult projectStepResult = new ProjectStepResult(stepVO, progress, onlineSchemaChangeProperties.isEnableFullVerify());
        PrecheckResult precheckResult = PrecheckResult.PASSED;
        projectStepResult.setPrecheckResult(precheckResult);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullTransferEstimatedCount(100);
        projectStepResult.setFullTransferFinishedCount(50);

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(mock(ProjectFullVerifyResultResponse.class)); // not necessary for this test

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> {
            ScheduleTaskEntity entity = invocation.getArgument(0);
            entity.setResultJson(JsonUtils.toJson(result));
            entity.setProgressPercentage(projectStepResult.getTaskPercentage());
            return entity;
        });

        valveContext = TestUtils.createValveContext(taskParameter);
        valveContext.setScheduleTask(new ScheduleTaskEntity().setId(1L).setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult())));
        scheduleCheckOmsProjectValve.invoke(valveContext);

        OnlineSchemaChangeScheduleTaskResult actualResult = JsonUtils.fromJson(valveContext.getScheduleTask().getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(result.getFullTransferProgressPercentage(), actualResult.getFullTransferProgressPercentage());

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(scheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
    }

    @Test(expected = OscException.class)
    public void invokeTestWithFailedPrecheck() {
        UUID projectId = UUID.randomUUID();
        String databaseName = "testDatabase";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId.toString());
        taskParameter.setDatabaseName(databaseName);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepVO stepVO = mock(ProjectStepVO.class);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(stepVO));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(progress);
        when(stepVO.getCurrentStep()).thenReturn("FULL_TRANSFER");
        when(stepVO.getStatus()).thenReturn(TaskStatus.RUNNING.name());
        when(progress.getCurrentStepStatus()).thenReturn(TaskStatus.RUNNING.name());

        PrecheckResult precheckResult = PrecheckResult.FAILED;
        projectStepResult.setPrecheckResult(precheckResult);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullTransferEstimatedCount(100);
        projectStepResult.setFullTransferFinishedCount(50);

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(mock(ProjectFullVerifyResultResponse.class)); // not necessary for this test

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> {
            ScheduleTaskEntity entity = invocation.getArgument(0);
            entity.setResultJson(JsonUtils.toJson(result));
            entity.setProgressPercentage(projectStepResult.getTaskPercentage());
            return entity;
        });

        valveContext = TestUtils.createValveContext(taskParameter);
        valveContext.setScheduleTask(new ScheduleTaskEntity().setId(1L).setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult())));
        scheduleCheckOmsProjectValve.invoke(valveContext);

        assertFalse(projectStepResult.isSuccess());
        assertTrue(projectStepResult.isThrowException());
        assertSame(ErrorCodes.OmsPreCheckFailed, projectStepResult.getErrorCode());
        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(scheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
    }

    @Test(expected = OscException.class)
    public void invokeTestWithInconsistentFullVerification() {
        UUID projectId = UUID.randomUUID();
        String databaseName = "testDatabase";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId.toString());
        taskParameter.setDatabaseName(databaseName);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepVO stepVO = mock(ProjectStepVO.class);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(stepVO));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(progress);
        when(stepVO.getCurrentStep()).thenReturn("FULL_VERIFIER");
        when(stepVO.getStatus()).thenReturn(TaskStatus.RUNNING.name());
        when(progress.getCurrentStepStatus()).thenReturn(TaskStatus.RUNNING.name());

        projectStepResult.setPrecheckResult(PrecheckResult.PASSED);
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectStepResult.setFullVerificationProgressPercentage(50);
        projectStepResult.setFullVerificationResultDescription("Table A has inconsistent data");
        projectStepResult.setFullTransferProgressPercentage(100);
        projectStepResult.setFullTransferEstimatedCount(100);
        projectStepResult.setFullTransferFinishedCount(100);

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(mock(ProjectFullVerifyResultResponse.class)); // not necessary for this test

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> {
            ScheduleTaskEntity entity = invocation.getArgument(0);
            entity.setResultJson(JsonUtils.toJson(result));
            entity.setProgressPercentage(projectStepResult.getTaskPercentage());
            return entity;
        });

        valveContext = TestUtils.createValveContext(taskParameter);
        valveContext.setScheduleTask(new ScheduleTaskEntity().setId(1L).setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult())));
        scheduleCheckOmsProjectValve.invoke(valveContext);

        assertFalse(projectStepResult.isSuccess());
        assertTrue(projectStepResult.isThrowException());
        assertSame(ErrorCodes.OmsDataCheckInconsistent, projectStepResult.getErrorCode());
        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(scheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
    }

    @Test
    public void invokeTestWithSucceededFullVerification() {
        UUID projectId = UUID.randomUUID();
        String databaseName = "testDatabase";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId.toString());
        taskParameter.setDatabaseName(databaseName);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepVO stepVO = mock(ProjectStepVO.class);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(stepVO));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(progress);
        when(stepVO.getCurrentStep()).thenReturn("FULL_VERIFIER");
        when(stepVO.getStatus()).thenReturn(TaskStatus.DONE.name());
        when(progress.getCurrentStepStatus()).thenReturn(TaskStatus.DONE.name());
        projectStepResult.setPrecheckResult(PrecheckResult.PASSED);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationProgressPercentage(100);
        projectStepResult.setFullVerificationResultDescription("All tables are consistent");
        projectStepResult.setFullTransferProgressPercentage(100);
        projectStepResult.setFullTransferEstimatedCount(100);
        projectStepResult.setFullTransferFinishedCount(100);

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(mock(ProjectFullVerifyResultResponse.class)); // not necessary for this test

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenAnswer(invocation -> {
            ScheduleTaskEntity entity = invocation.getArgument(0);
            entity.setResultJson(JsonUtils.toJson(result));
            entity.setProgressPercentage(projectStepResult.getTaskPercentage());
            return entity;
        });

        valveContext = TestUtils.createValveContext(taskParameter);
        valveContext.setScheduleTask(new ScheduleTaskEntity().setId(1L).setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult())));
        scheduleCheckOmsProjectValve.invoke(valveContext);

        assertTrue(projectStepResult.isSuccess());
        assertFalse(projectStepResult.isThrowException());
        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(scheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
    }
}
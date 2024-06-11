package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.After;
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
    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(projectOpenApiService, completeHandler, scheduleTaskRepository,
                onlineSchemaChangeProperties, tableFactory);
    }

    @Test
    public void testInvoke_whenProjectIsFinished_thenDoNothing() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        scheduleTask.setStatus(TaskStatus.FAILED.toString());

        valve.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        verify(projectOpenApiService, never()).describeProjectSteps(any());
        verify(projectOpenApiService, never()).describeProjectProgress(any());
    }

    @Test
    public void testInvoke_whenProjectIsRunning_thenCheckProjectStatus() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        scheduleTask.setStatus(TaskStatus.RUNNING.toString());

        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setCurrentStep(OmsStepName.FULL_TRANSFER.toString());
        projectStep.setStatus(TaskStatus.RUNNING.toString());
        progress.setSteps(Lists.newArrayList(projectStep));

        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(progress);

        valve.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        verify(projectOpenApiService).describeProjectProgress(any());
        verify(projectOpenApiService, never()).describeProjectSteps(any());
    }

    @Test
    public void testInvoke_whenProjectIsFailed_thenCheckProjectSteps() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        scheduleTask.setStatus(TaskStatus.FAILED.toString());

        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setCurrentStep(OmsStepName.FULL_TRANSFER.toString());
        projectStep.setStatus(TaskStatus.FAILED.toString());
        projectStep.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        progress.setSteps(Lists.newArrayList(projectStep));

        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(progress);

        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(new ProjectFullVerifyResultResponse());

        valve.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        verify(projectOpenApiService).describeProjectProgress(any());
        verify(projectOpenApiService).listProjectFullVerifyResult(any());
    }

    @Test
    public void testInvoke_whenProjectIsSuspended_thenCheckProjectSteps() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        scheduleTask.setStatus(TaskStatus.SUSPENDED.toString());

        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setCurrentStep(OmsStepName.FULL_TRANSFER.toString());
        projectStep.setStatus(TaskStatus.FAILED.toString());
        progress.setSteps(Lists.newArrayList(projectStep));

        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(progress);

        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(new ProjectFullVerifyResultResponse());

        valve.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        verify(projectOpenApiService).describeProjectProgress(any());
        verify(projectOpenApiService).listProjectFullVerifyResult(any());
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenProjectHasPrecheckError_thenThrowException() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        scheduleTask.setStatus(TaskStatus.RUNNING.toString());

        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setCurrentStep(OmsStepName.PRE_CHECK.toString());
        projectStep.setStatus(TaskStatus.FAILED.toString());
        projectStep.setPrecheckResult(PrecheckResult.FAILED);
        progress.setSteps(Lists.newArrayList(projectStep));

        String errorMsg = "Precheck error";
        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(new ProjectFullVerifyResultResponse());

        valve.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        verify(projectOpenApiService).describeProjectProgress(any());
        verify(projectOpenApiService).listProjectFullVerifyResult(any());
    }

    @Test
    public void testInvoke_whenProjectIsRunningAndFullTransferIsDone_thenSwapTables()
            throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        scheduleTask.setStatus(TaskStatus.RUNNING.toString());

        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setCurrentStep(OmsStepName.FULL_TRANSFER.toString());
        projectStep.setStatus(TaskStatus.DONE.toString());
        projectStep.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        progress.setSteps(Lists.newArrayList(projectStep));

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep(projectStep);
        projectStepResult.setFullVerificationResult(projectStep.getFullVerificationResult());
        projectStepResult.setFullTransferProgressPercentage(100);

        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(new ProjectFullVerifyResultResponse());
        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);
        when(tableFactory.generateTable(any(), any()))
                .thenAnswer(i -> i.getArgumentAt(0, 0).toString());

        valve.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        verify(projectOpenApiService).resumeProject(any());
        verify(completeHandler).handle(any());
        verify(projectOpenApiService, atLeastOnce()).describeProjectSteps(any());
        verify(projectOpenApiService, atLeastOnce()).describeProjectProgress(any());
        verify(projectOpenApiService, atLeastOnce()).listProjectFullVerifyResult(any());
        verify(scheduleTaskRepository).update(any());
    }

    @Test
    public void testInvoke_whenProjectIsRunningAndFullTransferIsNotDone_thenDoNothing() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        scheduleTask.setStatus(TaskStatus.RUNNING.toString());

        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setCurrentStep(OmsStepName.FULL_TRANSFER.toString());
        projectStep.setStatus(TaskStatus.RUNNING.toString());
        progress.setSteps(Lists.newArrayList(projectStep));

        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(new ProjectFullVerifyResultResponse());

        valve.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        verify(projectOpenApiService, never()).resumeProject(any());
        verify(completeHandler, never()).handle(any());
        verify(projectOpenApiService, atLeastOnce()).describeProjectSteps(any());
        verify(projectOpenApiService, atLeastOnce()).describeProjectProgress(any());
        verify(projectOpenApiService, atLeastOnce()).listProjectFullVerifyResult(any());
        verify(scheduleTaskRepository).update(any());
    }

    @Test
    public void testInvoke_whenProjectIsRunningAndFullTransferIsFailed_thenDoNothing() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        scheduleTask.setStatus(TaskStatus.RUNNING.toString());

        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setCurrentStep(OmsStepName.FULL_TRANSFER.toString());
        projectStep.setStatus(TaskStatus.FAILED.toString());
        progress.setSteps(Lists.newArrayList(projectStep));

        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(new ProjectFullVerifyResultResponse());

        valve.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        verify(projectOpenApiService, never()).resumeProject(any());
        verify(completeHandler, never()).handle(any());
        verify(projectOpenApiService, atLeastOnce()).describeProjectSteps(any());
        verify(projectOpenApiService, atLeastOnce()).describeProjectProgress(any());
        verify(projectOpenApiService, atLeastOnce()).listProjectFullVerifyResult(any());
        verify(scheduleTaskRepository).update(any());
    }

    @Test
    public void testInvoke_whenProjectIsFailedAndFullTransferIsNotDone_thenDoNothing() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(UUID.randomUUID().toString());
        scheduleTask.setStatus(TaskStatus.FAILED.toString());

        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setCurrentStep(OmsStepName.FULL_TRANSFER.toString());
        projectStep.setStatus(TaskStatus.RUNNING.toString());
        progress.setSteps(Lists.newArrayList(projectStep));

        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(new ProjectFullVerifyResultResponse());

        valve.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        verify(projectOpenApiService, never()).resumeProject(any());
        verify(completeHandler, never()).handle(any());
        verify(projectOpenApiService, atLeastOnce()).describeProjectSteps(any());
        verify(projectOpenApiService, atLeastOnce()).describeProjectProgress(any());
        verify(projectOpenApiService, atLeastOnce()).listProjectFullVerifyResult(any());
        verify(scheduleTaskRepository).update(any());
    }
}
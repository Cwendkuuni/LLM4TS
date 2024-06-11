package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

/**
 * ScheduleCheckOmsProjectValveTest
 */
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Mock
    private DefaultTableFactory defaultTableFactory;

    @Mock
    private SwapTableUtil swapTableUtil;

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectControlRequest projectRequest;
    private ListProjectFullVerifyResultRequest listProjectFullVerifyResultRequest;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = new ScheduleTaskEntity();
        projectRequest = new ProjectControlRequest();
        listProjectFullVerifyResultRequest = new ListProjectFullVerifyResultRequest();
        projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
    }

    @Test
    public void testInvoke_whenProjectStepsAndProgressAreReturned_thenScheduleTaskIsUpdated() {
        // given
        ProjectStepVO projectStepVO = new ProjectStepVO();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        // when
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // then
        verify(scheduleTaskRepository).update(scheduleTask);
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenProjectStepsAreReturnedButProjectIsFailed_thenThrowOscException() {
        // given
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStepStatus(TaskStatus.FAILED.name());
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        // when, then
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenProjectStepsAreNotFound_thenThrowOscException() {
        // given
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenThrow(new RuntimeException());

        // when, then
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenProjectProgressIsNotFound_thenThrowOscException() {
        // given
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenThrow(new RuntimeException());

        // when, then
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectIsRunning_thenPluginContinues() {
        // given
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStepStatus(TaskStatus.RUNNING.name());
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        // when
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // then
        verify(getNext()).invoke(mock(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectIsSuspended_thenPluginContinues() {
        // given
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStepStatus(TaskStatus.SUSPENDED.name());
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        // when
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // then
        verify(getNext()).invoke(mock(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectIsFinished_thenPluginContinues() {
        // given
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStepStatus(TaskStatus.FINISHED.name());
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        // when
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // then
        verify(getNext()).invoke(mock(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectIsFailed_thenPluginContinues() {
        // given
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStepStatus(TaskStatus.FAILED.name());
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        // when
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // then
        verify(getNext()).invoke(mock(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectHasInconsistentData_thenThrowOscException() {
        // given
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        // when, then
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectHasPreCheckFailed_thenThrowOscException() {
        // given
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setPreCheckResult(PrecheckResult.FAILED);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        // when, then
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));
    }

    @Test
    public void testAdaptResult() {
        // given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferEstimatedCount(10);
        projectStepResult.setFullTransferFinishedCount(5);
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationResultDescription("desc");
        projectStepResult.setFullVerificationProgressPercentage(50);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        // when
        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        // then
        assertEquals(5, result.getFullTransferEstimatedCount());
        assertEquals(5, result.getFullTransferFinishedCount());
        assertEquals(50, result.getFullTransferProgressPercentage().intValue());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals("desc", result.getFullVerificationResultDescription());
        assertEquals(50, result.getFullVerificationProgressPercentage().intValue());
    }

    @Test
    public void testListProjectFullVerifyResult() {
        // given
        when(projectOpenApiService.listProjectFullVerifyResult(listProjectFullVerifyResultRequest)).thenReturn(projectFullVerifyResultResponse);

        // when
        List<ProjectFullVerifyResultResponse> result = scheduleCheckOmsProjectValve.listProjectFullVerifyResult("1", "db1", UUID.randomUUID().toString());

        // then
        assertEquals(projectFullVerifyResultResponse, result.get(0));
    }

    @Test(expected = OscException.class)
    public void testListProjectFullVerifyResult_whenApiReturnEmptyList_thenThrowOscException() {
        // given
        when(projectOpenApiService.listProjectFullVerifyResult(listProjectFullVerifyResultRequest)).thenReturn(new ProjectFullVerifyResultResponse[0]);

        // when, then
        scheduleCheckOmsProjectValve.listProjectFullVerifyResult("1", "db1", UUID.randomUUID().toString());
    }

    @Test
    public void testRecordCurrentProgress() {
        // given
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep("fullTransfer");
        result.setCurrentStepStatus(TaskStatus.RUNNING.name());
        result.setFullTransferProgressPercentage(50);
        result.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        result.setFullVerificationResultDescription("desc");
        result.setFullVerificationProgressPercentage(50);

        // when
        scheduleCheckOmsProjectValve.recordCurrentProgress(result);

        // then
        String[] headers = getHeader().toArray(new String[0]);
        String[] body = new String[] {"1", "fullTransfer", "RUNNING", "50", "desc"};
        Table table = new DefaultTableFactory().generateTable(headers.length, headers, body);
        String expectedTable = table.render().toString();

        assertEquals(expectedTable, log.getTrace());
    }
}
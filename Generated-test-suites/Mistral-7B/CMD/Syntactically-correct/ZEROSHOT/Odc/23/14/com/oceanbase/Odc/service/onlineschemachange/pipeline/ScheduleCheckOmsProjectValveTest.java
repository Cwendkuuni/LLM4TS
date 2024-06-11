package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectOpenApiService;
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
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private ScheduleTaskEntity scheduleTask;
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;

    @Before
    public void init() {
        this.taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        this.scheduleTask = new ScheduleTaskEntity();
    }

    @Test
    public void testInvoke() throws Exception {
        // given
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(getProjectProgress());
        when(projectOpenApiService.resumeProject(any(ProjectControlRequest.class)))
                .thenReturn(null);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(getProjectFullVerifyResult());

        ProjectStepResult projectStepResult = new ProjectStepResult(OmsStepName.PRE_CHECK,
                PrecheckResult.PASSED, "preCheckResultDescription");

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(
                this.taskParameter);
        expectedResult.setFullTransferEstimatedCount(1L);
        expectedResult.setFullTransferFinishedCount(1L);
        expectedResult.setFullTransferProgressPercentage(100);
        expectedResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        expectedResult.setFullVerificationProgressPercentage(100);
        expectedResult.setCurrentStep(OmsStepName.FULL_TRANSFER.name());
        expectedResult.setCurrentStepStatus(TaskStatus.DONE.name());

        // when
        scheduleCheckOmsProjectValve.invoke(getValveContext());

        // then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(expectedResult, result);
    }

    private ValveContext getValveContext() {
        ValveContext context = mock(ValveContext.class);
        when(context.getScheduleTask()).thenReturn(this.scheduleTask);
        when(context.getTaskParameter()).thenReturn(this.taskParameter);
        return context;
    }

    private List<ProjectStepVO> getProjectSteps() {
        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setStepName(OmsStepName.PRE_CHECK.name());
        projectStep.setStatus(TaskStatus.DONE.name());
        projectSteps.add(projectStep);
        return projectSteps;
    }

    private ProjectProgressResponse getProjectProgress() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        projectProgressResponse.setProgressPercentage(100);
        return projectProgressResponse;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResult() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectFullVerifyResultResponse.setFullVerificationProgressPercentage(100);
        return projectFullVerifyResultResponse;
    }
}
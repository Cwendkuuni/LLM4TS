package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

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

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = new ScheduleTaskEntity();
    }

    @Test
    public void testHandleOmsProjectStepResult_taskStatusIsDone_fullVerificationResultIsConsistent() {
        // given
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(mockProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(mockProjectProgress());

        ProjectStepResult projectStepResult = new ProjectStepResult(OmsStepName.FULL_VERIFIER, FullVerificationResult.CONSISTENT);
        ProjectFullVerifyResultResponse fullVerifyResultResponse = mockProjectFullVerifyResultResponse();

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        // when
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(mockValveContext(), projectStepResult, result,
                SwapTableType.DATABASE, scheduleTask);

        // then
        verify(projectOpenApiService).resumeProject(projectRequest);
        verify(scheduleTaskRepository).update(scheduleTask);
    }

    @Test
    public void testHandleOmsProjectStepResult_taskStatusIsDone_fullVerificationResultIsUncheck() {
        // given
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(mockProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(mockProjectProgress());

        ProjectStepResult projectStepResult = new ProjectStepResult(OmsStepName.FULL_VERIFIER,
                FullVerificationResult.UNCHECK);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        // when
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(mockValveContext(), projectStepResult, result,
                SwapTableType.DATABASE, scheduleTask);

        // then
        verify(projectOpenApiService, never()).resumeProject(projectRequest);
        verify(scheduleTaskRepository).update(scheduleTask);
    }

    @Test(expected = OscException.class)
    public void testHandleOmsProjectStepResult_projectStepResultHasFailedPreCheckResult() {
        // given
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(mockProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(mockProjectProgress());

        ProjectStepResult projectStepResult = new ProjectStepResult(OmsStepName.PRE_CHECK, PrecheckResult.FAILED);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        // when
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(mockValveContext(), projectStepResult, result,
                SwapTableType.DATABASE, scheduleTask);

        // then
        throw new AssertionError();
    }

    @Test(expected = OscException.class)
    public void testHandleOmsProjectStepResult_projectStepResultHasInconsistentFullVerificationResult() {
        // given
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(mockProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(mockProjectProgress());

        ProjectStepResult projectStepResult = new ProjectStepResult(OmsStepName.FULL_VERIFIER,
                FullVerificationResult.INCONSISTENT);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        // when
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(mockValveContext(), projectStepResult, result,
                SwapTableType.DATABASE, scheduleTask);

        // then
        throw new AssertionError();
    }

    @Test
    public void testInvoke() {
        // given
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(mockProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(mockProjectProgress());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid("uid");
        taskParameter.setSwapTableType(SwapTableType.DATABASE);

        scheduleTask.setId("taskId");
        scheduleTask.setStatus("SCHEDULED");

        // when
        scheduleCheckOmsProjectValve.invoke(mockValveContext(taskParameter));

        // then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
    }

    private ProjectProgressResponse mockProjectProgress() {
        ProjectProgressResponse projectProgressResponse = mock(ProjectProgressResponse.class);
        when(projectProgressResponse.getSteps()).thenReturn(Mockito.mock(List.class));
        when(projectProgressResponse.getCurrentStep()).thenReturn("CURRENT_STEP");
        when(projectProgressResponse.getCurrentStepStatus()).thenReturn("CURRENT_STATUS");
        when(projectProgressResponse.getProgress()).thenReturn(1L);
        return projectProgressResponse;
    }

    private List<ProjectStepVO> mockProjectSteps() {
        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setName("CURRENT_STEP");
        projectStepVO.setStatus("CURRENT_STATUS");
        projectSteps.add(projectStepVO);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        return projectSteps;
    }

    private ProjectFullVerifyResultResponse mockProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        when(projectFullVerifyResultResponse.getTotalCount()).thenReturn(1L);
        when(projectFullVerifyResultResponse.getResults()).thenReturn(Mockito.mock(List.class));
        return projectFullVerifyResultResponse;
    }

    private ValveContext mockValveContext() {
        return mock(ValveContext.class);
    }

    private ValveContext mockValveContext(OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        ValveContext valveContext = mock(ValveContext.class);
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        when(valveContext.getTaskParameter()).thenReturn(taskParameter);
        return valveContext;
    }
}
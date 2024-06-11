package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

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

@InjectMocks
public class ScheduleCheckOmsProjectValveTest {

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
    public void init() {
        MockitoAnnotations.initMocks(this);
        valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setCompleteHandler(completeHandler);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
    }

    @Test
    public void testInvokeSuccess() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = getProjectSteps();
        ProjectProgressResponse progress = getProjectProgressResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("{}");
        scheduleTask.setStatus(TaskStatus.INIT);

        valve.invoke(getValveContext(scheduleTask, taskParameter));

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test
    public void testInvokeFailed() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);

        List<ProjectStepVO> projectSteps = getProjectSteps();
        ProjectProgressResponse progress = getProjectProgressResponseWithFailedStep();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("{}");
        scheduleTask.setStatus(TaskStatus.INIT);

        try {
            valve.invoke(getValveContext(scheduleTask, taskParameter));
            fail("Expected OscException to be thrown");
        } catch (OscException e) {
            assertEquals(ErrorCodes.OmsPreCheckFailed, e.getCode());
        }
    }

    @Test
    public void testInvokeFailedInconsistentData() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);

        List<ProjectStepVO> projectSteps = getProjectSteps();
        ProjectProgressResponse progress = getProjectProgressResponseWithInconsistentData();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("{}");
        scheduleTask.setStatus(TaskStatus.INIT);

        try {
            valve.invoke(getValveContext(scheduleTask, taskParameter));
            fail("Expected OscException to be thrown");
        } catch (OscException e) {
            assertEquals(ErrorCodes.OmsDataCheckInconsistent, e.getCode());
        }
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(projectRequest.getId()).thenReturn(taskParam.getOmsProjectId());
        when(projectRequest.getUid()).thenReturn(taskParam.getUid());
        return projectRequest;
    }

    private List<ProjectStepVO> getProjectSteps() {
        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setStatus(TaskStatus.DONE);
        projectStep.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectSteps.add(projectStep);
        return projectSteps;
    }

    private ProjectProgressResponse getProjectProgressResponse() {
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        when(progress.getCurrentStep()).thenReturn(null);
        return progress;
    }

    private ProjectProgressResponse getProjectProgressResponseWithFailedStep() {
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        when(progress.getCurrentStep()).thenReturn(new ProjectStepVO());
        when(progress.getCurrentStep().getStatus()).thenReturn(TaskStatus.FAILED);
        return progress;
    }

    private ProjectProgressResponse getProjectProgressResponseWithInconsistentData() {
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        when(progress.getCurrentStep()).thenReturn(new ProjectStepVO());
        when(progress.getCurrentStep().getFullVerificationResult()).thenReturn(FullVerificationResult.INCONSISTENT);
        return progress;
    }

    private ValveContext getValveContext(ScheduleTaskEntity scheduleTask, OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        OscValveContext context = mock(OscValveContext.class);
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParameter);
        return context;
    }
}
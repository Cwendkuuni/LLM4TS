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
import com.oceanbase.odc.common.constant.ErrorCodes;
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
import com.oceanbase.odc.service.onlineschemachange.utils.TableUtils;

@SuppressWarnings("unchecked")
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
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        valve = new ScheduleCheckOmsProjectValve();
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = new ScheduleTaskEntity();
    }

    @Test
    public void testInvoke_success() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(getProjectStepVO());
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectFullVerifyResultResponse verifyResult = getProjectFullVerifyResultResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress, verifyResult);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class))).thenReturn(verifyResult);

        // When
        valve.invoke(new OscValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameter);
        }});

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(onlineSchemaChangeProperties.isEnableFullVerify(), result.isFullVerificationEnabled());
        assertNull(result.getErrorMsg());
        assertEquals(TaskStatus.RUNNING, result.getStatus());

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    @Test
    public void testInvoke_precheckFailed() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(getProjectStepVO());
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress, null, PrecheckResult.FAILED, "Precheck failed");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        valve.invoke(new OscValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameter);
        }});

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(ErrorCodes.OmsPreCheckFailed, result.getErrorCode());
        assertEquals("Precheck failed", result.getErrorMsg());
        assertEquals(TaskStatus.FAILED, result.getStatus());

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verifyZeroInteractions(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    @Test
    public void testInvoke_fullVerifyInconsistent() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(getProjectStepVO());
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress, null, null, null, FullVerificationResult.INCONSISTENT, "Data inconsistent");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        valve.invoke(new OscValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameter);
        }});

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(ErrorCodes.OmsDataCheckInconsistent, result.getErrorCode());
        assertEquals("Task failed for origin table has inconsistent data with new table, result: Data inconsistent", result.getErrorMsg());
        assertEquals(TaskStatus.FAILED, result.getStatus());

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verifyZeroInteractions(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    @Test
    public void testInvoke_fullVerifyUncheck() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(getProjectStepVO());
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress, null, null, null, FullVerificationResult.UNCHECK, null);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        valve.invoke(new OscValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameter);
        }});

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertNull(result.getErrorCode());
        assertNull(result.getErrorMsg());
        assertEquals(TaskStatus.RUNNING, result.getStatus());

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verifyZeroInteractions(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    @Test
    public void testInvoke_resumeProjectSuccess() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(null, progress, null, null, null, null, null);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(null);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        // When
        valve.invoke(new OscValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameter);
        }});

        // Then
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
        verifyZeroInteractions(projectOpenApiService).describeProjectSteps(projectRequest);
        verifyZeroInteractions(projectOpenApiService).describeProjectProgress(projectRequest);
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(UUID.randomUUID().toString());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }

    private ProjectStepVO getProjectStepVO() {
        ProjectStepVO stepVO = new ProjectStepVO();
        stepVO.setName(OmsStepName.PRE_CHECK.name());
        stepVO.setStatus(TaskStatus.RUNNING.toString());
        return stepVO;
    }

    private ProjectProgressResponse getProjectProgressResponse() {
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setTotal(100);
        progress.setCurrent(50);
        return progress;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse verifyResult = new ProjectFullVerifyResultResponse();
        verifyResult.setStatus("FINISHED");
        verifyResult.setResult("Consistent");
        verifyResult.setMessage("Success");
        return verifyResult;
    }
}
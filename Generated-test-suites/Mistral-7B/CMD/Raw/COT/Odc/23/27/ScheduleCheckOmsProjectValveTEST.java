package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
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
 * Test class for ScheduleCheckOmsProjectValve
 */
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_whenScheduleTaskIsNotEmpty_shouldUpdateScheduleTask() {
        String projectId = "projectId";
        String databaseName = "databaseName";
        UUID uid = UUID.randomUUID();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName(databaseName);
        taskParameter.setUid(uid.toString());

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId("1");
        scheduleTask.setResultJson("");
        scheduleTask.setStatus(TaskStatus.SCHEDULED.toString());

        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(projectRequest.getId()).thenReturn(projectId);

        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(projectFullVerifyResultResponse);

        ProjectProgressResponse projectProgressResponse = mock(ProjectProgressResponse.class);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferProgressPercentage(100);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        OnlineSchemaChangeScheduleTaskResult result = scheduleCheckOmsProjectValve.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        assertEquals(TaskStatus.PROCESSING.toString(), result.getStatus());
        assertEquals(100, result.getFullTransferProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenProjectHasFailedFullCheck_shouldThrowOscException() {
        String projectId = "projectId";
        String databaseName = "databaseName";
        UUID uid = UUID.randomUUID();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName(databaseName);
        taskParameter.setUid(uid.toString());

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId("1");
        scheduleTask.setResultJson("");
        scheduleTask.setStatus(TaskStatus.SCHEDULED.toString());

        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(projectRequest.getId()).thenReturn(projectId);

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(null);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(null);

        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        OnlineSchemaChangeScheduleTaskResult result = scheduleCheckOmsProjectValve.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        throw new OscException(ErrorCodes.OmsDataCheckInconsistent, "Task failed");
    }

    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferEstimatedCount(100);
        projectStepResult.setFullTransferFinishedCount(50);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationProgressPercentage(50);

        ScheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        assertEquals(100, result.getFullTransferEstimatedCount());
        assertEquals(50, result.getFullTransferFinishedCount());
        assertEquals(50, result.getFullTransferProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals(50, result.getFullVerificationProgressPercentage());
        assertEquals(null, result.getCurrentStep());
        assertEquals(null, result.getCurrentStepStatus());
        assertEquals(null, result.getPrecheckResult());
    }

    @Test
    public void testGetProjectRequest() {
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);
        assertEquals(taskParam.getOmsProjectId(), projectRequest.getId());
        assertEquals(taskParam.getUid(), projectRequest.getUid());
    }

    @Test
    public void testRecordCurrentProgress_shouldPrintTable() {
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep(OmsStepName.FULL_TRANSFER.name());
        result.setCurrentStepStatus(TaskStatus.DONE.toString());
        result.setFullTransferProgressPercentage(50);
        result.setFullVerificationResultDescription("test description");

        ScheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);
    }
}

enum FullVerificationResult {
    CONSISTENT,
    INCONSISTENT,
    UNCHECK
}
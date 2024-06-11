package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        ProjectProgressResponse progressResponse = mock(ProjectProgressResponse.class);
        ProjectStepVO projectStepVO = mock(ProjectStepVO.class);
        ProjectFullVerifyResultResponse fullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        when(scheduleTaskRepository.findById("1")).thenReturn(Optional.of(scheduleTask));

        // When
        scheduleCheckOmsProjectValve.invoke(null);

        // Then
        // Add assertions here
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress,
                onlineSchemaChangeProperties.isEnableFullVerify());

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(progress);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(projectSteps);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(mock(ProjectFullVerifyResultResponse.class));

        when(scheduleTaskRepository.findById("1")).thenReturn(Optional.of(scheduleTask));

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult, null, SwapTableType.LOCAL,
                scheduleTask);

        // Then
        // Add assertions here
    }

    @Test
    public void testListProjectFullVerifyResult() {
        // Given
        String projectId = "1";
        String databaseName = "db1";
        String uid = "uid1";

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();

        // When
        ProjectFullVerifyResultResponse response = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId,
                databaseName, uid);

        // Then
        assertEquals(200, response.getCode());
        // Add other assertions here
    }

    @Test
    public void testGetProjectRequest() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

        // Then
        assertEquals(taskParam.getOmsProjectId(), projectRequest.getId());
        assertEquals(taskParam.getUid(), projectRequest.getUid());
    }

    @Test
    public void testInvokeWithException() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        ProjectProgressResponse progressResponse = mock(ProjectProgressResponse.class);
        ProjectStepVO projectStepVO = mock(ProjectStepVO.class);
        ProjectFullVerifyResultResponse fullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenThrow(new RuntimeException());

        when(scheduleTaskRepository.findById("1")).thenReturn(Optional.of(scheduleTask));

        // When and Then
        // Add assertions for exception here
    }

    @Test
    @Ignore
    public void testRecordCurrentProgress() {
        // Given
        String projectId = "1";
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(null);

        // When
        scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);

        // Then
        // Add assertions here
    }
}
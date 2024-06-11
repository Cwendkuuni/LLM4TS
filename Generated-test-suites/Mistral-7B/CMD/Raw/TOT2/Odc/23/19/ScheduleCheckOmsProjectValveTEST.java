package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

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
    @Mock
    private DefaultTableFactory defaultTableFactory;

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private ListProjectFullVerifyResultRequest listProjectFullVerifyResultRequest;
    private ProjectControlRequest projectControlRequest;
    private ScheduleTaskEntity scheduleTaskEntity;
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private OnlineSchemaChangeScheduleTaskResult result;
    private ProjectStepResult projectStepResult;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;
    private ProjectProgressResponse projectProgressResponse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        scheduleCheckOmsProjectValve.setDefaultTableFactory(defaultTableFactory);

        listProjectFullVerifyResultRequest = mock(ListProjectFullVerifyResultRequest.class);
        projectControlRequest = mock(ProjectControlRequest.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        projectStepResult = mock(ProjectStepResult.class);
        projectFullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        projectProgressResponse = mock(ProjectProgressResponse.class);
    }

    @Test
    public void testInvoke_whenProjectHasFullySuccessed_thenUpdateScheduleTaskAndNextInvoke() {
        // Given
        when(projectOpenApiService.describeProjectSteps(projectControlRequest)).thenReturn(
                Lists.newArrayList(new ProjectStepVO()));
        when(projectOpenApiService.describeProjectProgress(projectControlRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(listProjectFullVerifyResultRequest)).thenReturn(
                projectFullVerifyResultResponse);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        when(projectStepResult.getCurrentStepStatus()).thenReturn(TaskStatus.DONE.name());
        when(projectStepResult.getPreCheckResult()).thenReturn(PrecheckResult.PASSED);

        // When
        scheduleCheckOmsProjectValve.invoke(new OscValveContext());

        // Then
        verify(scheduleTaskRepository).update(scheduleTaskEntity);
        getNext().invoke(new OscValveContext());
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenProjectStepFailed_thenThrowOscException() {
        // Given
        when(projectOpenApiService.describeProjectSteps(projectControlRequest)).thenReturn(
                Lists.newArrayList(projectStepResult));
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.INCONSISTENT);

        // When & Then
        scheduleCheckOmsProjectValve.invoke(new OscValveContext());
    }
}
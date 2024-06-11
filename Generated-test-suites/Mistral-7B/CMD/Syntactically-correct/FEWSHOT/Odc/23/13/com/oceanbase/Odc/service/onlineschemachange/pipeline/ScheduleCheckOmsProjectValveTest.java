package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

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
@Component
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_withValidProjectStepResult_setsResultAndUpdatesScheduleTask() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        when(projectStepResult.getPreCheckResult()).thenReturn(PrecheckResult.PASSED);

        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        List<ProjectStepVO> projectSteps = mock(List.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectFullVerifyResultResponse fullVerifyResponse = mock(ProjectFullVerifyResultResponse.class);
        when(projectOpenApiService.listProjectFullVerifyResult(new ListProjectFullVerifyResultRequest())).thenReturn(fullVerifyResponse);

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(Optional.empty());

        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(scheduleTask.getId()).thenReturn(1L);

        // When
        valve.invoke(mock(ValveContext.class));

        // Then
        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        expectedResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        assertThat(valve.getLastResult()).isEqualTo(expectedResult);
        assertThat(scheduleTask.getResultJson()).isEqualTo(JsonUtils.toJson(expectedResult));
    }

    @Test
    public void testInvoke_withInvalidProjectStepResult_throwsOscException() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.INCONSISTENT);

        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        List<ProjectStepVO> projectSteps = mock(List.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When & Then
        assertThrows(OscException.class, () -> valve.invoke(mock(ValveContext.class)));
    }
}
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

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

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectStepResult projectStepResult;
    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setOscTaskCompleteHandler(completeHandler);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        scheduleCheckOmsProjectValve.setTableFactory(tableFactory);

        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = new ScheduleTaskEntity();
        projectStepResult = new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT);
        result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
    }

    @Test
    public void testInvoke_ProjectStepsAndProgressAreFetchedFromOms() {
        // Given
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        scheduleCheckOmsProjectValve.invoke(getValveContext());

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
    }

    @Test
    public void testInvoke_ProjectStepResultIsChecked() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult expectedProjectStepResult = new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectStepChecker().checkProjectStepResult(progress, projectSteps, onlineSchemaChangeProperties.isEnableFullVerify()))
                .thenReturn(expectedProjectStepResult);

        // When
        scheduleCheckOmsProjectValve.invoke(getValveContext());

        // Then
        ProjectStepResult actualProjectStepResult = projectStepResult;
        assertEquals("ProjectSteps and Progress are fetched from Oms, and ProjectStepResult is checked",
                expectedProjectStepResult, actualProjectStepResult);
    }

    @Test
    public void testInvoke_ProjectStepResultIsHandled() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult expectedProjectStepResult = new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectStepChecker().checkProjectStepResult(progress, projectSteps, onlineSchemaChangeProperties.isEnableFullVerify()))
                .thenReturn(expectedProjectStepResult);

        // When
        scheduleCheckOmsProjectValve.invoke(getValveContext());

        // Then
        ProjectStepResult actualProjectStepResult = projectStepResult;
        assertEquals("ProjectSteps and Progress are fetched from Oms, and ProjectStepResult is checked and handled",
                expectedProjectStepResult, actualProjectStepResult);
        verify(completeHandler).handle(eq(expectedProjectStepResult));
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test
    public void testInvoke_ProjectStepResultIsNotHandled_ProjectIsNotFinished() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult expectedProjectStepResult = new ProjectStepResult(TaskStatus.RUNNING, FullVerificationResult.UNCHECK);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectStepChecker().checkProjectStepResult(progress, projectSteps, onlineSchemaChangeProperties.isEnableFullVerify()))
                .thenReturn(expectedProjectStepResult);

        // When
        scheduleCheckOmsProjectValve.invoke(getValveContext());

        // Then
        ProjectStepResult actualProjectStepResult = projectStepResult;
        assertEquals("ProjectSteps and Progress are fetched from Oms, and ProjectStepResult is checked but not handled",
                expectedProjectStepResult, actualProjectStepResult);
        verifyNoMoreInteractions(completeHandler);
    }

    @Test
    public void testInvoke_ProjectStepResultIsNotHandled_ProjectIsFailed() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult expectedProjectStepResult = new ProjectStepResult(TaskStatus.FAILED, FullVerificationResult.INCONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectStepChecker().checkProjectStepResult(progress, projectSteps, onlineSchemaChangeProperties.isEnableFullVerify()))
                .thenThrow(new OscException(ErrorCodes.OmsDataCheckInconsistent, "Test error message"));

        // When
        scheduleCheckOmsProjectValve.invoke(getValveContext());

        // Then
        throwException("OscException is thrown when checking ProjectStepResult", expectedException(OscException.class));
    }

    @Test
    public void testInvoke_ProjectStepResultIsNotHandled_ProjectPrecheckFailed() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult expectedProjectStepResult = new ProjectStepResult(TaskStatus.FAILED, PrecheckResult.FAILED);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectStepChecker().checkProjectStepResult(progress, projectSteps, onlineSchemaChangeProperties.isEnableFullVerify()))
                .thenReturn(expectedProjectStepResult);

        // When
        scheduleCheckOmsProjectValve.invoke(getValveContext());

        // Then
        throwException("OscException is thrown when ProjectStepResult indicates PrecheckFailed",
                expectedException(OscException.class));
    }

    @Test
    public void testInvoke_ProjectStepResultIsNotHandled_ProjectFullcheckFailed() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult expectedProjectStepResult = new ProjectStepResult(TaskStatus.FAILED, FullVerificationResult.INCONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectStepChecker().checkProjectStepResult(progress, projectSteps, onlineSchemaChangeProperties.isEnableFullVerify()))
                .thenReturn(expectedProjectStepResult);

        // When
        scheduleCheckOmsProjectValve.invoke(getValveContext());

        // Then
        throwException("OscException is thrown when ProjectStepResult indicates FullCheckFailed",
                expectedException(OscException.class));
    }

    @Test
    public void testInvoke_ProjectStepResultIsNotHandled_ProjectStepNotFound() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult expectedProjectStepResult = null;

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectStepChecker().checkProjectStepResult(progress, projectSteps, onlineSchemaChangeProperties.isEnableFullVerify()))
                .thenReturn(expectedProjectStepResult);

        // When
        scheduleCheckOmsProjectValve.invoke(getValveContext());

        // Then
        verifyNoMoreInteractions(projectOpenApiService);
        verifyNoMoreInteractions(projectStepChecker());
        verifyNoMoreInteractions(completeHandler);
    }

    private OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
        return taskParameter;
    }

    private ScheduleTaskEntity getScheduleTask() {
        return scheduleTask;
    }

    private ProjectControlRequest getProjectRequest() {
        return mock(ProjectControlRequest.class);
    }

    private ProjectProgressResponse getProjectProgressResponse() {
        return mock(ProjectProgressResponse.class);
    }

    private ProjectStepVO getProjectStepVO() {
        return mock(ProjectStepVO.class);
    }

    private ProjectStepResultChecker projectStepChecker() {
        return spy(new ProjectStepResultChecker(mock(ProjectStepVO.class), mock(ProjectProgressResponse.class),
                onlineSchemaChangeProperties, projectOpenApiService, tableFactory));
    }

    private ValveContext getValveContext() {
        OscValveContext oscValveContext = mock(OscValveContext.class);
        when(oscValveContext.getScheduleTask()).thenReturn(getScheduleTask());
        when(oscValveContext.getTaskParameter()).thenReturn(getTaskParameter());
        return new ValveContext() {
            @Override
            public OscValveContext getOscValveContext() {
                return oscValveContext;
            }
        };
    }

    private void throwException(String message, Class<? extends Throwable> expectedExceptionType) {
        try {
            throw new AssertionError(message);
        } catch (AssertionError e) {
            if (expectedExceptionType.isInstance(e)) {
                throw e;
            } else {
                throw new AssertionError("Unexpected exception type", e);
            }
        }
    }

    private <T extends Throwable> T expectedException(Class<T> expectedExceptionType) {
        return mock(expectedExceptionType);
    }
}
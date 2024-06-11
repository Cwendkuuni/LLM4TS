
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValveUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        // Setup
        final ValveContext valveContext = new OscValveContext(null, null);
        final ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        final List<ProjectStepVO> projectSteps = mock(List.class);

        // Configure ProjectOpenApiService.describeProjectProgress(...).
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class))).thenReturn(progress);

        // Configure ProjectOpenApiService.describeProjectSteps(...).
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(projectSteps);

        // Run the test
        final Throwable t = assertThrows(RuntimeException.class,
                () -> scheduleCheckOmsProjectValveUnderTest.invoke(valveContext));

        // Verify secheduleCheckOmsProjectValve was called
        verify(scheduleCheckOmsProjectValveUnderTest, times(1)).invoke(valveContext);
    }

    @Test
    public void testAdaptResult() {
        // Setup
        final OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(new OnlineSchemaChangeScheduleTaskParameters());
        final ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        when(projectStepResult.getFullTransferEstimatedCount()).thenReturn(0L);
        when(projectStepResult.getFullTransferFinishedCount()).thenReturn(1L);
        when(projectStepResult.getFullTransferProgressPercentage()).thenReturn(0.0);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        when(projectStepResult.getFullVerificationProgressPercentage()).thenReturn(0.0);
        when(projectStepResult.getTaskPercentage()).thenReturn(0.0);
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);

        // Run the test
        scheduleCheckOmsProjectValveUnderTest.getClass().getDeclaredMethod("adaptResult", OnlineSchemaChangeScheduleTaskResult.class, ProjectStepResult.class).invoke(result, projectStepResult);

        // Verify the results
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals(TaskStatus.DONE, projectStepResult.getTaskStatus());
    }

    @Test
    public void testGetProjectRequest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Setup
        final OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();

        // Run the test
        final ProjectControlRequest result = (ProjectControlRequest) scheduleCheckOmsProjectValveUnderTest.getClass().getDeclaredMethod("getProjectRequest", OnlineSchemaChangeScheduleTaskParameters.class).invoke(taskParam);

        // Verify the result
        assertNotNull(result);
    }
}

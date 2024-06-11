
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService mockProjectOpenApiService;

    @Mock
    private ScheduleTaskRepository mockScheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties mockOnlineSchemaChangeProperties;

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValveUnderTest;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValveUnderTest = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValveUnderTest.projectOpenApiService = mockProjectOpenApiService;
        scheduleCheckOmsProjectValveUnderTest.scheduleTaskRepository = mockScheduleTaskRepository;
        scheduleCheckOmsProjectValveUnderTest.onlineSchemaChangeProperties = mockOnlineSchemaChangeProperties;
    }

    @Test
    public void testInvoke() {

        ScheduleTaskEntity mockScheduleTask = mock(ScheduleTaskEntity.class);
        OscValveContext mockOscValveContext = mock(OscValveContext.class);
        when(mockOscValveContext.getScheduleTask()).thenReturn(mockScheduleTask);

        ProjectControlRequest mockProjectRequest = mock(ProjectControlRequest.class);
        ProjectProgressResponse mockProjectProgressResponse = mock(ProjectProgressResponse.class);

        when(mockProjectOpenApiService.describeProjectProgress(mockProjectRequest)).thenReturn(mockProjectProgressResponse);
        when(mockProjectOpenApiService.describeProjectSteps(mockProjectRequest)).thenReturn(null);

        ProjectFullVerifyResultResponse mockResponse = new ProjectFullVerifyResultResponse();
        when(mockProjectOpenApiService.listProjectFullVerifyResult(any())).thenReturn(mockResponse);

        scheduleCheckOmsProjectValveUnderTest.invoke(mockOscValveContext);

        verify(mockScheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResult_PrecheckResult_Failed() {
        ProjectStepResult mockProjectStepResult = new ProjectStepResult();
        mockProjectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        scheduleCheckOmsProjectValveUnderTest.continueHandleProjectStepResult(mockProjectStepResult);
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResult_FullVerificationResult_Inconsistent() {
        ProjectStepResult mockProjectStepResult = new ProjectStepResult();
        mockProjectStepResult.setPreCheckResult(PrecheckResult.PASSED);
        mockProjectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        scheduleCheckOmsProjectValveUnderTest.continueHandleProjectStepResult(mockProjectStepResult);
    }

    @Test
    public void testContinueHandleProjectStepResult_Pass() {
        ProjectStepResult mockProjectStepResult = new ProjectStepResult();
        mockProjectStepResult.setPreCheckResult(PrecheckResult.PASSED);
        mockProjectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        scheduleCheckOmsProjectValveUnderTest.continueHandleProjectStepResult(mockProjectStepResult);
    }
}


package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve testSubject;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskEntity scheduleTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldInvoke() {
        OscValveContext context = mock(OscValveContext.class);
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);

        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));
        when(context.getParameter().getSwapTableType()).thenReturn(SwapTableType.MANUAL_SEMI_AUTO);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(mock(ProjectProgressResponse.class));
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class))).thenReturn(mock(ProjectFullVerifyResultResponse.class));

        testSubject.invoke(context);

        verify(scheduleTaskRepository).update(scheduleTask);
        verify(completeHandler, times(1)).handle(any());
    }

    @Test
    public void shouldRecordCurrentProgress() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep(OmsStepName.FULL_VERIFIER.name());
        result.setFullVerificationProgressPercentage(100.0);
        result.setFullVerificationResultDescription("Finished");

        testSubject.recordCurrentProgress("testProjectId", result);

        verify(log, times(1)).info(any());
    }
}

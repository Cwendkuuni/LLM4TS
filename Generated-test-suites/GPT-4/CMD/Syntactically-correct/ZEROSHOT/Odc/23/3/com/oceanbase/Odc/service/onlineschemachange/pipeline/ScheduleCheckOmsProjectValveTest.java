package com.oceanbase.odc.service.onlineschemachange.pipeline;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.onlineschemachange.pipeline.*;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;

public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OscValveContext context;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Mock
    private ScheduleTaskEntity scheduleTask;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;

    @Mock
    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParameter);

        scheduleCheckOmsProjectValve.invoke(context);

        verify(context, times(1)).getScheduleTask();
        verify(context, times(1)).getTaskParameter();
    }

    @Test
    public void testAdaptResult() {
        scheduleCheckOmsProjectValve.adaptResult(result, new ProjectStepResult());

        verify(result, times(1)).setFullTransferEstimatedCount(any());
        verify(result, times(1)).setFullTransferFinishedCount(any());
        verify(result, times(1)).setFullTransferProgressPercentage(any());
        verify(result, times(1)).setFullVerificationResult(any());
        verify(result, times(1)).setFullVerificationResultDescription(any());
        verify(result, times(1)).setFullVerificationProgressPercentage(any());
        verify(result, times(1)).setCurrentStep(any());
        verify(result, times(1)).setCurrentStepStatus(any());
        verify(result, times(1)).setPrecheckResult(any());
    }

    @Test
    public void testGetProjectRequest() {
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setUid("uid");
        taskParameters.setOmsProjectId("projectId");

        ProjectControlRequest projectControlRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameters);

        assert("uid".equals(projectControlRequest.getUid()));
        assert("projectId".equals(projectControlRequest.getId()));
    }
}

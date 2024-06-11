
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.google.common.collect.Lists;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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
    private OscValveContext oscValveContext;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult;

    @Before
    public void setUp() throws Exception {
        when(oscValveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);
        when(oscValveContext.getTaskParameter()).thenReturn(onlineSchemaChangeScheduleTaskResult);
    }

    @Test
    public void invokeTest() {
        //create some sample response and mocks as per your requirements.
        ProjectStepResult projectStepResultMock = mock(ProjectStepResult.class);
        when(projectStepResultMock.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        when(projectStepResultMock.getTaskStatus()).thenReturn(TaskStatus.DONE);

        scheduleCheckOmsProjectValve.invoke(oscValveContext);

        //verify your methods
        verify(scheduleTaskRepository, atLeastOnce()).update(any(ScheduleTaskEntity.class));
        verify(oscValveContext, atLeastOnce()).getScheduleTask();
        verify(projectOpenApiService, atLeastOnce()).describeProjectSteps(any(ProjectControlRequest.class));
        verify(onlineSchemaChangeProperties, atLeastOnce()).isEnableFullVerify();
    }
}

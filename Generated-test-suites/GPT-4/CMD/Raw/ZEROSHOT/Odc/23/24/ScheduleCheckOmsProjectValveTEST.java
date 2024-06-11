
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    ScheduleTaskEntity scheduleTask;
    @Mock
    ProjectControlRequest projectRequest;
    @Mock
    ProjectStepResult projectStepResult;
    @Mock
    OnlineSchemaChangeScheduleTaskParameters taskParameter;
    @Mock
    OscValveContext context;
    @Mock
    ValveContext valveContext;
    @Mock
    ProjectFullVerifyResultResponse fullVerifyResultResponse;

    @InjectMocks
    ScheduleCheckOmsProjectValve valveUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInvoke() {
        // Setting up mock behavior
        when(context.getTaskParameter()).thenReturn(taskParameter);
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(valveUnderTest.getProjectRequest(taskParameter)).thenReturn(projectRequest);
        when(valveUnderTest.describeProjectSteps(projectRequest)).thenReturn(Collections.emptyList());
        when(valveUnderTest.describeProjectProgress(projectRequest)).thenReturn(null);
        when(valveUnderTest.listProjectFullVerifyResult(any(), any(), any())).thenReturn(null);

        // Executing the method under test
        valveUnderTest.invoke(context);

        // Verifications
        verify(context, times(2)).getTaskParameter();
        verify(valveUnderTest, times(1)).getProjectRequest(taskParameter);
        verify(valveUnderTest, times(1)).describeProjectSteps(projectRequest);
        verify(valveUnderTest, times(1)).describeProjectProgress(projectRequest);
        verify(valveUnderTest, times(1)).listProjectFullVerifyResult(any(), any(), any());
    }

    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        valveUnderTest.adaptResult(result, projectStepResult);
        
        // Add any necessary assertions or verifications here
    }

    @Test
    public void testGetProjectRequest() {
        valveUnderTest.getProjectRequest(taskParameter);

        // Add any necessary assertions or verifications here
    }

    @Test
    public void testListProjectFullVerifyResult() {
        valveUnderTest.listProjectFullVerifyResult("projectId", "databaseName", "uid");

        // Add any necessary assertions or verifications here
    }
}

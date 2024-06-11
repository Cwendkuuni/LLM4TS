import static org.mockito.Mockito.*;

import org.junit.Test;

public class ScheduleCheckOmsProjectValveTest {

    @Test
    public void testInvoke() {
        // Setup
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        
        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext oscValveContext = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        when(oscValveContext.getScheduleTask()).thenReturn(scheduleTask);
        when(oscValveContext.getTaskParameter()).thenReturn(taskParameter);
        
        // Test
        scheduleCheckOmsProjectValve.invoke(valveContext);
        
        // Verify
        verify(scheduleTask, times(1)).getResultJson();
        verify(scheduleTask, times(1)).setResultJson(anyString());
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);
        verify(projectOpenApiService, times(3)).describeProjectSteps(any());
        verify(projectOpenApiService, times(1)).describeProjectProgress(any());
        verify(projectOpenApiService, times(1)).resumeProject(any());
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(), any(), any());
        verify(oscTaskCompleteHandler, times(1)).handleOmsProjectStepResult(any(), any(), any(), any(), any());
        verify(log, times(2)).debug(anyString(), anyString(), anyString());
    }
}
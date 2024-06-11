import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    @Test
    public void invoke_Success() {
        // Prepare
        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext context = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        when(context.getTaskParameter()).thenReturn(taskParameter);
        
        // Test
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.invoke(valveContext);

        // Verify
        verify(scheduleTask, times(1)).getResultJson();
        verify(scheduleTask, times(1)).setResultJson(anyString());
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);
        verify(projectOpenApiService, times(1)).resumeProject(any());
    }

    @Test
    public void handleOmsProjectStepResult_Done_Consistent() {
        // Prepare
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        ValveContext valveContext = mock(ValveContext.class);

        // Test
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.handleOmsProjectStepResult(valveContext, projectStepResult);

        // Verify
        verify(scheduleTask, times(2)).setResultJson(anyString());
        verify(scheduleTaskRepository, times(2)).update(scheduleTask);
        verify(valveContext, times(1)).invoke(any());
    }

    @Test
    public void continueHandleProjectStepResult_PreCheckFailed() {
        // Prepare
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        // Test
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();

        // Verify
        assertThrows(OscException.class, () -> valve.continueHandleProjectStepResult(projectStepResult));
    }

    // Additional test cases for other methods can be added here

}
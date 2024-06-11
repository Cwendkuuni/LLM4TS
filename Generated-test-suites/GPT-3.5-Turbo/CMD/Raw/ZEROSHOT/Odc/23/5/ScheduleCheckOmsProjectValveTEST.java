import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
    }

    @Test
    public void testInvoke() {
        // Mock ValveContext, OscValveContext, ScheduleTaskEntity, OnlineSchemaChangeScheduleTaskParameters
        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext oscValveContext = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameters = mock(OnlineSchemaChangeScheduleTaskParameters.class);

        // Set up mock interactions
        when(oscValveContext.getScheduleTask()).thenReturn(scheduleTaskEntity);
        when(oscValveContext.getTaskParameter()).thenReturn(taskParameters);

        // Call invoke method and verify interactions
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Verify that the methods were called
        verify(oscValveContext, times(1)).getScheduleTask();
        verify(oscValveContext, times(1)).getTaskParameter();
        verify(scheduleTaskEntity, times(1)).getResultJson();
        verify(scheduleTaskEntity, times(1)).setResultJson(anyString());
        verify(scheduleTaskEntity, times(1)).getProgressPercentage();
        verify(scheduleTaskEntity, times(1)).setProgressPercentage(anyInt());
        verify(scheduleTaskEntity, times(1)).getStatus();
        verify(scheduleTaskEntity, times(1)).setStatus(anyString());
        verify(scheduleTaskEntity, times(1)).getId();
        verify(scheduleTaskEntity, times(1)).setId(anyString());
        verify(scheduleTaskEntity, times(1)).getUid();
        verify(scheduleTaskEntity, times(1)).setUid(anyString());
        verify(scheduleTaskEntity, times(1)).getDatabaseName();
        verify(scheduleTaskEntity, times(1)).setDatabaseName(anyString());
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Mock ValveContext, ProjectStepResult, OnlineSchemaChangeScheduleTaskResult, SwapTableType, ScheduleTaskEntity
        ValveContext valveContext = mock(ValveContext.class);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
        SwapTableType swapTableType = mock(SwapTableType.class);
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);

        // Set up mock interactions
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        when(result.isManualSwapTableEnabled()).thenReturn(false);
        when(result.isManualSwapTableStarted()).thenReturn(false);
        when(swapTableType.isSwapTableEnable(any(), any(), any(), any(), any())).thenReturn(true);

        // Call handleOmsProjectStepResult method and verify interactions
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTaskEntity);

        // Verify that the methods were called
        verify(result, times(2)).isManualSwapTableEnabled();
        verify(result, times(2)).isManualSwapTableStarted();
        verify(scheduleTaskEntity, times(2)).getResultJson();
        verify(scheduleTaskEntity, times(2)).update(scheduleTaskEntity);
        verify(swapTableType, times(1)).isSwapTableEnable(any(), any(), any(), any(), any());
    }

}
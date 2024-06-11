import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    @Test
    public void testInvoke() {
        // Setup
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext context = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(context.getScheduleTask()).thenReturn(scheduleTask);

        // Test invoke method
        valve.invoke(valveContext);

        // Verify if the methods are called
        verify(context, times(1)).getScheduleTask();
        verify(scheduleTask, times(1)).getId();
    }

    @Test
    public void testHandleProjectStepResult() {
        // Setup
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ValveContext valveContext = mock(ValveContext.class);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
        SwapTableType swapTableType = SwapTableType.AUTO_SWAP;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

        // Test handleOmsProjectStepResult method
        valve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTask);

        // Verify if the methods are called
        verify(projectStepResult, times(1)).getTaskStatus();
        verify(scheduleTask, times(1)).getResultJson();
    }

    @Test
    public void testListProjectFullVerifyResult() {
        // Setup
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        String projectId = "1234";
        String databaseName = "test";
        String uid = "5678";

        // Test listProjectFullVerifyResult method
        ProjectFullVerifyResultResponse response = valve.listProjectFullVerifyResult(projectId, databaseName, uid);

        // Verify if the response is not null
        assertNotNull(response);
    }

    @Test
    public void testAdaptResult() {
        // Setup
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        // Test adaptResult method
        valve.adaptResult(result, projectStepResult);

        // Verify if the result is updated correctly
        assertEquals(result.getFullTransferEstimatedCount(), projectStepResult.getFullTransferEstimatedCount());
    }

    @Test
    public void testRecordCurrentProgress() {
        // Setup
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        String projectId = "1234";
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);

        // Test recordCurrentProgress method
        valve.recordCurrentProgress(projectId, result);

        // No need to verify as the method only logs info
    }
}
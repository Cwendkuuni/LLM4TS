package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ScheduleCheckOmsProjectValveTest {

    @Test
    public void testHandleOmsProjectStepResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
        SwapTableType swapTableType = SwapTableType.AUTO;

        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(scheduleTask.getStatus()).thenReturn(TaskStatus.DONE);

        valve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTask);

        verify(result).setManualSwapTableEnabled(eq(true));
        verify(scheduleTaskRepository).update(eq(scheduleTask));
        verifyNoMoreInteractions(result, scheduleTaskRepository);
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setErrorMsg("Precheck failed");

        try {
            valve.continueHandleProjectStepResult(projectStepResult);
        } catch(OscException e) {
            assertEquals(ErrorCodes.OmsPreCheckFailed, e.getErrorCode());
            assertEquals("Precheck failed", e.getMessage());
        }
    }

    @Test
    public void testListProjectFullVerifyResult() {
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ProjectFullVerifyResultResponse response = valve.listProjectFullVerifyResult("projectId", "databaseName", "uid");

        assertNotNull(response);
        assertEquals("projectId", response.getProjectId());
        assertEquals("databaseName", response.getDatabaseName());
        assertEquals("uid", response.getUid());
    }
}
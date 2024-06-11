package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ScheduleCheckOmsProjectValveTest {

    @Test
    public void testInvoke() {
        // Test case for invoking the ScheduleCheckOmsProjectValve
        // Mocking required dependencies

        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext context = mock(OscValveContext.class);
        when(valveContext).thenReturn(context);

        ProjectStepResultChecker projectStepResultChecker = mock(ProjectStepResultChecker.class);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        when(projectStepResultChecker.getCheckerResult()).thenReturn(projectStepResult);

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("123");
        taskParameter.setDatabaseName("testdb");
        taskParameter.setUid("abc123");

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();

        // Setting up the environment for testing
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Verifying the results
        assertNotNull(scheduleCheckOmsProjectValve);
        // Add more assertions based on the specific requirements of the method
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Test case for handling Oms Project Step Result
        // Mocking required dependencies

        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext context = mock(OscValveContext.class);
        when(valveContext).thenReturn(context);

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setDatabaseName("testdb");
        taskParameter.setOmsProjectId("123");
        taskParameter.setUid("abc123");

        SwapTableType swapTableType = SwapTableType.AUTO;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus(TaskStatus.RUNNING);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();

        // Setting up the environment for testing
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTask);

        // Verifying the results
        assertNotNull(scheduleCheckOmsProjectValve);
        // Add more assertions based on the specific requirements of the method
    }
}
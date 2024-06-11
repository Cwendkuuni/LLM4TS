package com.oceanbase.odc.core.shared.constant;

import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class ScheduleCheckOmsProjectValveTest {

    @Test
    public void testInvoke() {

        // Set up scheduled task data, The context, and the scheduled task
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        OscValveContext context = Mockito.mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setUid("123");
        taskParameter.setDatabaseName("test");
        taskParameter.setOmsProjectId("100");

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParameter.getUid());
        projectRequest.setId(taskParameter.getOmsProjectId());

        // Mock the related methods
        Mockito.when(context.getScheduleTask()).thenReturn(scheduleTask);
        Mockito.when(context.getTaskParameter()).thenReturn(taskParameter);
        Mockito.doNothing().when(valve).invoke(context);
        Mockito.doNothing().when(scheduleTaskRepository).update(scheduleTask);

        // Invoke the method
        valve.invoke(context);

        // Validate the results
        Assert.assertEquals("123", taskParameter.getUid());
        Assert.assertEquals("test", taskParameter.getDatabaseName());
        Assert.assertEquals("100", taskParameter.getOmsProjectId());
        Mockito.verify(context, Mockito.times(1)).getScheduleTask();
        Mockito.verify(context, Mockito.times(1)).getTaskParameter();
    }

    @Test
    public void testAdaptResult() {
        // Test data
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        // Execute the method
        valve.adaptResult(result, projectStepResult);
        // Test assertions
        Assert.assertEquals(projectStepResult.getFullTransferEstimatedCount(), result.getFullTransferEstimatedCount());
        Assert.assertEquals(projectStepResult.getFullTransferFinishedCount(), result.getFullTransferFinishedCount());
        Assert.assertEquals(projectStepResult.getFullTransferProgressPercentage(), result.getFullTransferProgressPercentage());
        Assert.assertEquals(projectStepResult.getFullVerificationResult(), result.getFullVerificationResult());
        Assert.assertEquals(projectStepResult.getFullVerificationResultDescription(), result.getFullVerificationResultDescription());
        Assert.assertEquals(projectStepResult.getFullVerificationProgressPercentage(), result.getFullVerificationProgressPercentage());
        Assert.assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
        Assert.assertEquals(projectStepResult.getCurrentStepStatus(), result.getCurrentStepStatus());
        Assert.assertEquals(projectStepResult.getPreCheckResult(), result.getPrecheckResult());
    }
}
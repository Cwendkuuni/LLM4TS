
package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;

    @Before
    public void setUp() {
        this.dataArchiveRollbackJob = new DataArchiveRollbackJob();
        this.context = mock(JobExecutionContext.class);
        this.taskEntity = mock(ScheduleTaskEntity.class);
    }

    @Test
    public void testExecute() {
        // Prepare test data
        DataArchiveRollbackParameters rollbackParameters =
                new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("1");
        String parametersJson = JsonUtils.toJson(rollbackParameters);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson("");
        dataArchiveTask.setParametersJson(parametersJson);

        List<DlmTask> taskUnits = new ArrayList<>();

        // Mock the behaviors
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);
        when(dataArchiveRollbackJob.getTaskStatus(taskUnits).getStatus()).thenReturn(TaskStatus.PREPARING);

        // Execute the method
        dataArchiveRollbackJob.execute(context);

        // Assert the result
        Assert.assertEquals(TaskStatus.PREPARING, dataArchiveRollbackJob.getTaskStatus(taskUnits));

    }
}

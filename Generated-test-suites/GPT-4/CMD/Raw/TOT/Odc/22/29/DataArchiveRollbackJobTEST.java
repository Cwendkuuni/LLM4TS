
/*
 * Copyright (c) 2023 OceanBase.
 */
package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_noDataArchiveTask() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        
        // Assuming context returns a schedule task entity
        when(context.getResult()).thenReturn(taskEntity);
    
        // Add non-existing data archive task id
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":\"nonexistent\"}");

        dataArchiveRollbackJob.execute(context);

        // Check if the task status was updated to failed
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_existingDataArchiveTask() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();

        when(context.getResult()).thenReturn(taskEntity);

        // Add existing data archive task id
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":\"existing\"}");

        // Assuming dataArchiveTaskOption has few tasks
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(new ScheduleTaskEntity()));

        // ... additional verifications ...
    }

    @After
    public void tearDown() {
        dataArchiveRollbackJob = null;
    }
}

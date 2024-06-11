package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    JobExecutionContext context;

    @Test
    public void testExecute() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        // Test case 1: Data archive task not found
        when(context.getResult()).thenReturn(null);
        dataArchiveDeleteJob.execute(context);
        // Add assertion here

        // Test case 2: Data archive task status is not DONE
        // Mock dataArchiveTaskOption
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        List<DlmTask> taskUnits = new ArrayList<>();
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveDeleteJob.execute(context);
        // Add assertion here

        // Test case 3: Prepare tasks for clear
        // Mock dataArchiveTaskOption
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[]"); // empty list of DlmTask
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveDeleteJob.execute(context);
        // Add assertion here
    }
}
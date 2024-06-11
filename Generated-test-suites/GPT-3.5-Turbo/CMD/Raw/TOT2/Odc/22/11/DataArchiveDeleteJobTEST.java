
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute_FoundDataArchiveTask() {
        // Mock taskEntity and dataArchiveClearParameters
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        String dataArchiveTaskResult = "[{\"id\": 1, \"name\": \"task1\", \"status\": \"DONE\" }]";

        // Mock repository
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.updateStatusById(1, TaskStatus.DONE)).thenReturn();

        // Execute
        dataArchiveDeleteJob.execute(context);

        // Verify
        verify(scheduleTaskRepository).updateStatusById(1, TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Mock taskEntity and dataArchiveClearParameters
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();

        // Mock repository
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.empty());

        // Execute
        dataArchiveDeleteJob.execute(context);

        // Verify
        verify(scheduleTaskRepository).updateStatusById(1, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        // Mock taskEntity and dataArchiveClearParameters
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        String dataArchiveTaskResult = "[{\"id\": 1, \"name\": \"task1\", \"status\": \"PENDING\" }]";

        // Mock repository
        when(scheduleTaskRepository.findById(1)).thenReturn(taskEntity);
        when(scheduleTaskRepository.updateStatusById(1, TaskStatus.FAILED)).thenReturn();

        // Execute
        dataArchiveDeleteJob.execute(context);

        // Verify
        verify(scheduleTaskRepository).updateStatusById(1, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_PrepareTasksForClear() {
        // Mock dataArchiveTask
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1);
        dataArchiveTask.setResultJson("[{\"id\": 1, \"name\": \"task1\", \"status\": \"DONE\" }]");

        // Mock repository
        when(scheduleTaskRepository.updateStatusById(1, TaskStatus.PREPARING)).thenReturn();
      
        // Execute
        dataArchiveDeleteJob.execute(context);

        // Verify
        verify(scheduleTaskRepository).updateStatusById(1, TaskStatus.PREPARING);
    }

    @Test
    public void testExecute_ExecuteTask() {
        // Mock taskUnits
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setId(1);
        taskUnit1.setName("task1");
        taskUnit1.setStatus(TaskStatus.PENDING);
        taskUnits.add(taskUnit1);
        String taskUnitResult = "[{\"id\": 1, \"name\": \"task1\", \"jobType\": \"DELETE\", \"status\": \"PREPARING\" }]";
      
        // Mock repository
        when(scheduleTaskRepository.updateStatusById(1, TaskStatus.PREPARING)).thenReturn();

        // Execute
        dataArchiveDeleteJob.execute(context);

        // Verify
        verify(scheduleTaskRepository).updateStatusById(1, TaskStatus.PREPARING);
    }

}

package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    private DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

    @Test
    public void testExecute() {
        // Prepare test data
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);
        
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setStatus(TaskStatus.DONE);
        taskUnits.add(taskUnit);
        
        // Mock repository and context behavior
        
        // Test successful execution
        scheduleTaskRepository.findById(1L).thenReturn(Optional.of(dataArchiveTask));
        JsonUtils.fromJson(eq(dataArchiveTask.getResultJson()), any(TypeReference.class)).thenReturn(taskUnits);
        scheduleTaskRepository.updateStatusById(eq(1L), any(TaskStatus.class));
        assertEquals(TaskStatus.DONE, dataArchiveDeleteJob.execute(context));
        
        // Test task not found
        scheduleTaskRepository.findById(1L).thenReturn(Optional.empty());
        scheduleTaskRepository.updateStatusById(eq(1L), any(TaskStatus.class));
        assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.execute(context));
        
        // Test data archive task not finished
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        scheduleTaskRepository.findById(1L).thenReturn(Optional.of(dataArchiveTask));
        scheduleTaskRepository.updateStatusById(eq(1L), any(TaskStatus.class));
        assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.execute(context));
    }
}
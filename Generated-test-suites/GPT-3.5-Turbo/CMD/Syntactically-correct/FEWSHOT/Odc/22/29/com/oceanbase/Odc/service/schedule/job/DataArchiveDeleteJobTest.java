package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

public class DataArchiveDeleteJobTest {
    
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;
    
    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = new ScheduleTaskEntity();
        dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(123);
        
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        scheduleTaskEntity.setParametersJson("{ \"dataArchiveTaskId\": 123 }");
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }
    
    @Test
    public void testExecuteDataArchiveTaskNotDone() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.IN_PROGRESS);
        
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }
    
    @Test
    public void testPrepareTasksForClear() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1);
        dlmTask.setStatus(TaskStatus.DONE);
        
        taskUnits.add(dlmTask);
        
        dataArchiveTask.setResultJson("[{\"id\": 1, \"status\": \"DONE\"}]");
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(JobType.DELETE, dlmTask.getJobType());
        assertEquals(TaskStatus.PREPARING, dlmTask.getStatus());
    }
    
    @Test
    public void testUpdateStatusById() {
        TaskStatus taskStatus = TaskStatus.FINISHED;
        
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1);
        dlmTask1.setStatus(TaskStatus.DONE);
        
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(2);
        dlmTask2.setStatus(TaskStatus.FAILED);
        
        taskUnits.add(dlmTask1);
        taskUnits.add(dlmTask2);
        
        dataArchiveTask.setResultJson("[{\"id\": 1, \"status\": \"DONE\"}, {\"id\": 2, \"status\": \"FAILED\"}]");
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        assertEquals(taskStatus, scheduleTaskEntity.getStatus());
    }
}
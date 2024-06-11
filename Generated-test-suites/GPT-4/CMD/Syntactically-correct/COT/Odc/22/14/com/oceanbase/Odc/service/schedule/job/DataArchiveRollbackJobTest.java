package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
    private JobDataMap dataMap;
    private ScheduleTaskEntity entity;
    
    @Before
    public void setup() {
        entity = mock(ScheduleTaskEntity.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = new JobExecutionContextImpl(null, null, null);
        dataMap = new JobDataMap();

        when(context.getMergedJobDataMap()).thenReturn(dataMap);
        when(context.getResult()).thenReturn(entity);
    }

    @Test
    public void executeTest() {
        dataArchiveRollbackJob.execute(context);
        verify(context, times(2)).getResult();
    }
    
    @Test
    public void executeTest_noEntitiesInContext() {
        ScheduleTaskEntity taskEntity = null;
        when(context.getResult()).thenReturn(taskEntity);
        
        dataArchiveRollbackJob.execute(context);
        verify(context, times(1)).getResult();
    }
    
    @Test
    public void executeTest_rollbackTaskNotFound() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setResultJson("[]");

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }
    
    @Test(expected = SomeException.class) // replace SomeException with the exception that you think this method might throw
    public void executeTest_withException() {
        when(context.getResult()).thenThrow(SomeException.class); // replace SomeException with the exception that you think this method might throw

        dataArchiveRollbackJob.execute(context);
        
        //expect an exception to be thrown
    }
}

package com.oceanbase.odc.service.schedule.job;
/*

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import org.quartz.JobExecutionContext;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    // Typical Use Case
    @Test
    public void testExecute_success() {
        // mock context to return a valid ScheduleTaskEntity
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        when(context.getResult()).thenReturn(taskEntity);

        // mock scheduleTaskRepository to return a valid Optional<ScheduleTaskEntity>
        when(scheduleTaskRepository.findById(Mockito.anyString())).thenReturn(Optional.of(taskEntity));
        dataArchiveRollbackJob.execute(context);

        // Verify calls
        verify(scheduleTaskRepository, times(1)).findById(Mockito.anyString());
        verify(scheduleTaskRepository, times(1)).updateStatusById(Mockito.anyString(), Mockito.any(TaskStatus.class));

        //TODO : More assertions here
    }

    // Edge Case   
    @Test(expected = RuntimeException.class)
    public void testExecute_IllegalArgument() {
        // mock context to return null ScheduleTaskEntity
        when(context.getResult()).thenReturn(null);

        dataArchiveRollbackJob.execute(context);

        // Verify no interactions with the repository
        verify(scheduleTaskRepository, times(0)).findById(Mockito.anyString());
    }

    // Error Scenario
    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // mock context to return a valid ScheduleTaskEntity
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        when(context.getResult()).thenReturn(taskEntity);

        // mock scheduleTaskRepository to return an empty Optional<ScheduleTaskEntity>
        when(scheduleTaskRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        dataArchiveRollbackJob.execute(context);

        // Verify interactions with the repository
        verify(scheduleTaskRepository, times(1)).findById(Mockito.anyString());
        verify(scheduleTaskRepository, times(1)).updateStatusById(Mockito.anyString(), Mockito.eq(TaskStatus.FAILED));
    }
}

*/
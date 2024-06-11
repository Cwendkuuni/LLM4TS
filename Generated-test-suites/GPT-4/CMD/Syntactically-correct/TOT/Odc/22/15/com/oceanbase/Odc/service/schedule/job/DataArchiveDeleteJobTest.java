package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataArchiveDeleteJobTest {

    @Test
    public void test_execute() {
        // Create mock objects 
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("someJson");
        
        // Instantiate the class you're testing with the necessary mock objects
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.setScheduleTaskRepository(scheduleTaskRepository);
        
        // Run the method you're testing
        job.execute(context);
        
        // Verify that the methods you'd expect to be called were properly invoked
        verify(scheduleTaskRepository, times(1)).findById(any());
    }
}
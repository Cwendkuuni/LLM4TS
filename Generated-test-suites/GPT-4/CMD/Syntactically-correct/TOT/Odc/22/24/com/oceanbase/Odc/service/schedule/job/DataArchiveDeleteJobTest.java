package com.oceanbase.odc.service.schedule.job;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        // Create mock interfaces
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(scheduleTask);

        // Assume JsonUtils is a utility class with static methods...
        // You may have to refactor the JsonUtils.fromJson method into an instance method in a different class, to make it mockable.
        
        // Initialize class to be tested
        DataArchiveDeleteJob deleteJob = new DataArchiveDeleteJob();

        // Perform the test
        deleteJob.execute(context);

        // Add verification checks to confirm the desired interactions have taken place
        verify(context, times(1)).getResult();
        //...add more verifications...
    }
}
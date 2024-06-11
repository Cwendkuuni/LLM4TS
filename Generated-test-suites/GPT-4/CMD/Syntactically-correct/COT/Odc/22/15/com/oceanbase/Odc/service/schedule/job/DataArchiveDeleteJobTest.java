package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {
    @Mock
    JobExecutionContext context;

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":3}");

        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        job.execute(context);

        verify(context, times(1)).getResult();
    }
}
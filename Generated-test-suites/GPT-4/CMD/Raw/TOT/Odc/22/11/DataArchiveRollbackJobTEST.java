import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext jobExecutionContext;
   
    @Mock
    private ScheduleTaskEntity taskEntity;

    @Test
    public void testExecuteWithDataArchivePresent() throws JobExecutionException {
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, atLeastOnce()).updateStatusById(any(), any());
    }

    @Test
    public void testExecuteWithDataArchiveAbsent() throws JobExecutionException {
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, atLeastOnce()).updateStatusById(any(), eq(TaskStatus.FAILED));
    }

    @Test(expected = JobExecutionException.class)
    public void testExecuteWithException() throws JobExecutionException {
        when(jobExecutionContext.getResult()).thenThrow(new JobExecutionException());

        dataArchiveRollbackJob.execute(jobExecutionContext);
    }
}
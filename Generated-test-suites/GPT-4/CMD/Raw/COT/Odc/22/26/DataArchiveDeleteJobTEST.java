import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.quartz.JobExecutionContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    JobExecutionContext context;

    DataArchiveDeleteJob deleteJob = new DataArchiveDeleteJob();

    @Test
    public void testExecute(){
        deleteJob.execute(context); 
        // add verification and assertion...
    }

    @Test(expected = DataArchiveTaskNotFoundException.class)
    public void testExecute_InvalidDataArchiveTaskId(){
        deleteJob.execute(context); 
        // add verification and assertion...
    }

    @Test(expected = TaskNotFinishedException.class)
    public void testExecute_TaskNotFinishedException(){
        deleteJob.execute(context); 
        // add verification and assertion...
    }
}
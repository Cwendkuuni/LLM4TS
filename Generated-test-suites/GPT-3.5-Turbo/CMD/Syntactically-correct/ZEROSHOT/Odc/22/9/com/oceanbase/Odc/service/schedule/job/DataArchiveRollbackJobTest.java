import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    private DataArchiveRollbackJob job = new DataArchiveRollbackJob();

    @Test
    public void testExecute() {
        // Setup
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        taskEntity.setResultJson("[]");

        when(context.getResult()).thenReturn(taskEntity);

        // Execute
        job.execute(context);

        // Verify
        assertEquals(Thread.currentThread(), job.getJobThread());
        // Additional verifications can be added here based on the test case requirements
    }
}
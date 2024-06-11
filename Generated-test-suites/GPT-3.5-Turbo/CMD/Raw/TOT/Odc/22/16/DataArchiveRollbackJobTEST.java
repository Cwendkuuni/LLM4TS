import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext mockJobContext;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_WhenDataArchiveTaskNotFound_ReturnFailedStatus() {
        // Setup
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        when(mockJobContext.getResult()).thenReturn(taskEntity);

        // Test
        dataArchiveRollbackJob.execute(mockJobContext);

        // Verify
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_WhenDataArchiveTaskFound_PrepareTasksForRollback() {
        // Setup
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        DlmTask taskUnit2 = new DlmTask();
        taskUnits.add(taskUnit1);
        taskUnits.add(taskUnit2);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        when(mockJobContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        // Test
        dataArchiveRollbackJob.execute(mockJobContext);

        // Verify
        assertEquals(JobType.ROLLBACK, taskUnit1.getJobType());
        assertEquals(TaskStatus.PREPARING, taskUnit2.getStatus());
    }

}
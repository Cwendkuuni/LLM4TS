import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_Success() {
        // Mock data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(2);
        taskUnit.setTargetDatabaseId(3);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        // Stubbing
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.of(dataArchiveTask));

        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        job.execute(jobExecutionContext);

        // Verification
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecute_TaskNotFound() {
        // Mock data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1);

        // Stubbing
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.empty());

        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        job.execute(jobExecutionContext);

        // Verification
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
}
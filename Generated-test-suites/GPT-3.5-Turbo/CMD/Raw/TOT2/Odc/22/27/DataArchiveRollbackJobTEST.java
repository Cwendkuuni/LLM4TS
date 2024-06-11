import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.common.Constants;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_Success() {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(123);
        taskEntity.setParametersJson(Constants.OBJECT_MAPPER.writeValueAsString(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(123);
        dataArchiveTask.setResultJson(Constants.OBJECT_MAPPER.writeValueAsString(Arrays.asList(new DlmTask())));
        List<DlmTask> expectedTaskUnits = Arrays.asList(new DlmTask());

        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(scheduleTaskRepository.findById(123)).thenReturn(Optional.of(dataArchiveTask));

        // Act
        dataArchiveRollbackJob.execute(context);

        // Assert
        Mockito.verify(scheduleTaskRepository).updateStatusById(123, TaskStatus.DONE);
    }

    @Test
    public void testExecute_TaskNotFound() {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(123);
        taskEntity.setParametersJson(Constants.OBJECT_MAPPER.writeValueAsString(rollbackParameters));

        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(scheduleTaskRepository.findById(123)).thenReturn(Optional.empty());

        // Act
        dataArchiveRollbackJob.execute(context);

        // Assert
        Mockito.verify(scheduleTaskRepository).updateStatusById(Mockito.anyInt(), TaskStatus.FAILED);
    }

    // Add more test cases for edge cases and exception handling scenarios

}
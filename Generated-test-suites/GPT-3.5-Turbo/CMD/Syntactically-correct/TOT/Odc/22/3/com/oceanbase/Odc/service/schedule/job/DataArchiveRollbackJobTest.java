
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute() {
        // Typical Use Case
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{}");

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.DONE));

        // Edge Case
        TaskStatus taskStatus = TaskStatus.PREPARING;
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setSourceDatabaseId(2L);
        taskUnit.setTargetDatabaseId(3L);
        taskUnit.setJobType(JobType.ROLLBACK);
        taskUnit.setStatus(taskStatus);
        taskUnits.add(taskUnit);
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.DONE));

        // Error Scenario
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }
}

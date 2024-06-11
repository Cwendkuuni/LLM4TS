import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute() {
        // Arrange
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setResultJson(JsonUtils.toJson(new ArrayList<DlmTask>()));
        
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        // Act
        dataArchiveRollbackJob.execute(context);

        // Assert
        verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.DONE));
    }
}
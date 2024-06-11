import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.JobType;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.DlmJobIdUtil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ObjectMapper objectMapper;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        objectMapper = Mockito.spy(new ObjectMapper());
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        doReturn(Optional.empty()).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());

        // when & then
        try {
            dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));
            verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(any(Long.class), any(TaskStatus.class));
        } catch (JobExecutionException e) {
            // expected
        }
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());

        List<DlmTask> tasks = Mockito.mock(List.class);

        // when
        dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

        // then
        verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(any(Long.class), any(TaskStatus.class));
        verify(objectMapper, Mockito.times(1)).fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {});
        verify(dataArchiveRollbackJob, Mockito.times(1)).executeTask(any(Long.class), eq(tasks));
    }

    @Test
    public void testExecute_TaskStatusUpdated() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> tasks = Mockito.mock(List.class);

        doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());
        doReturn(tasks).when(objectMapper).fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {});

        TaskStatus expectedTaskStatus = TaskStatus.FAILED;

        // when
        dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

        // then
        verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(any(Long.class), eq(expectedTaskStatus));
    }
}
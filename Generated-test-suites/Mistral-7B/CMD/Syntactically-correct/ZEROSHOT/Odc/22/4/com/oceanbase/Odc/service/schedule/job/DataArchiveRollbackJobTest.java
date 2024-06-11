import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.DlmJobIdUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Test class for DataArchiveRollbackJob
 */
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        // given
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters =
                new DataArchiveRollbackParameters("1", "test task");
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // when
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // then
        Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws Exception {
        // given
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters =
                new DataArchiveRollbackParameters("1", "test task");
        List<DlmTask> dlmTasks = new ArrayList<>();
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        Mockito.when(scheduleTaskRepository.getById(taskEntity.getId())).thenReturn(taskEntity);

        DlmTask dlmTask = Mockito.mock(DlmTask.class);
        dlmTasks.add(dlmTask);
        Mockito.when(taskEntity.getParametersJson()).thenReturn("{}");
        Mockito.when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJsonString(dlmTasks));

        // when
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // then
        Mockito.verify(taskEntity).setStatus(TaskStatus.DONE);
        Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_ExceptionThrown() throws Exception {
        // given
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters =
                new DataArchiveRollbackParameters("1", "test task");
        List<DlmTask> dlmTasks = new ArrayList<>();
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        Mockito.when(taskEntity.getId()).thenReturn(1L);

        DlmTask dlmTask = Mockito.mock(DlmTask.class);
        dlmTasks.add(dlmTask);
        Mockito.when(taskEntity.getParametersJson()).thenReturn("{}");
        Mockito.when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJsonString(dlmTasks));

        Mockito.doThrow(new RuntimeException("Test exception")).when(dataArchiveRollbackJob)
                .executeTask(taskEntity.getId(), dlmTasks);

        // when
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // then
        Mockito.verify(taskEntity).setStatus(TaskStatus.FAILED);
        Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
}
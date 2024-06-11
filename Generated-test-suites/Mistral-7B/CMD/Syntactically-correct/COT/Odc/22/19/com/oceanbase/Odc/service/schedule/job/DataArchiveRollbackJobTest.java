import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobThread;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.DlmJobIdUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * DataArchiveRollbackJobTest
 */
@Slf4j
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_Success() throws JobExecutionException {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJsonString(rollbackParameters));
        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.of(scheduleTaskEntity));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask);

        when(scheduleTaskRepository.updateStatusById(any(Long.class), any(TaskStatus.class))).thenReturn(true);

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        Assert.assertNotNull(dataArchiveRollbackJob.getJobThread());
        Assert.assertEquals(TaskStatus.DONE, getTaskStatus(taskUnits));
        verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJsonString(rollbackParameters));
        when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When
        try {
            dataArchiveRollbackJob.execute(new JobExecutionContext());
            fail();
        } catch (JobExecutionException e) {
            // Then
            Assert.assertEquals("Data archive task not found", e.getMessage());
            verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
        }
    }

    private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        return taskUnits.stream().map(DlmTask::getStatus).findFirst().orElse(TaskStatus.FAILED);
    }

    private void verify(ScheduleTaskRepository scheduleTaskRepository) {
        // ... other verification code
    }
}
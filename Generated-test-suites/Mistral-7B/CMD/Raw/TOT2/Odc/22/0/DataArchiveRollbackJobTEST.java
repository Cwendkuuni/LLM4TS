import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for DataArchiveRollbackJob.
 */
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @SuppressWarnings("unchecked")
    private final ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_DataArchiveTaskFound_TasksExecutedSuccessfully() throws Exception {
        // Given
        String jsonString = "{\"id\":1,\"parametersJson\":\"{\"dataArchiveTaskId\":1,\"jobName\":\"jobName\",\"jobGroup\":\"jobGroup\",\"frequency\":\"frequency\"}\"}";
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(jsonString);
        scheduleTaskEntity.setJobName("jobName");
        scheduleTaskEntity.setJobGroup("jobGroup");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);

        List<DlmTask> dlmTasks = Lists.newArrayList();
        dlmTasks.add(createDlmTask());

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        verifyTasksExecutedSuccessfully(dlmTasks);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound_TaskStatusUpdatedToFailed() throws Exception {
        // Given
        String jsonString = "{\"id\":1,\"parametersJson\":\"{\"dataArchiveTaskId\":1,\"jobName\":\"jobName\",\"jobGroup\":\"jobGroup\",\"frequency\":\"frequency\"}\"}";
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(jsonString);
        scheduleTaskEntity.setJobName("jobName");
        scheduleTaskEntity.setJobGroup("jobGroup");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.empty();
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        assertEquals(TaskStatus.FAILED, getLastTaskStatus(scheduleTaskEntity));
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_ExceptionThrownDuringTaskExecution() throws Exception {
        // Given
        String jsonString = "{\"id\":1,\"parametersJson\":\"{\"dataArchiveTaskId\":1,\"jobName\":\"jobName\",\"jobGroup\":\"jobGroup\",\"frequency\":\"frequency\"}\"}";
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(jsonString);
        scheduleTaskEntity.setJobName("jobName");
        scheduleTaskEntity.setJobGroup("jobGroup");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);

        List<DlmTask> dlmTasks = Lists.newArrayList();
        dlmTasks.add(createDlmTask());

        when(dataArchiveRollbackJob.executeTask(scheduleTaskEntity.getId(), dlmTasks)).thenThrow(new RuntimeException("Exception message"));

        // When and Then
        dataArchiveRollbackJob.execute(new JobExecutionContext());
        verifyTasksNotExecuted(dlmTasks);
    }

    private DlmTask createDlmTask() {
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        return dlmTask;
    }

    private void verifyTasksExecutedSuccessfully(List<DlmTask> dlmTasks) {
        for (DlmTask dlmTask : dlmTasks) {
            assertEquals(TaskStatus.DONE, getTaskStatus(dlmTask));
        }
    }

    private void verifyTasksNotExecuted(List<DlmTask> dlmTasks) {
        for (DlmTask dlmTask : dlmTasks) {
            assertEquals(TaskStatus.PREPARING, getTaskStatus(dlmTask));
        }
    }

    private TaskStatus getLastTaskStatus(ScheduleTaskEntity scheduleTaskEntity) {
        return scheduleTaskEntity.getStatus();
    }

    private TaskStatus getTaskStatus(DlmTask dlmTask) {
        return dlmTask.getStatus();
    }
}
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters.DataArchiveRollbackParametersBuilder;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import mockito.InjectMocks;
import mockito.Mock;
import mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test class for DataArchiveRollbackJob.
 */
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

    /**
     * Test method for execute(JobExecutionContext).
     * Given: A valid DataArchiveRollbackParameters and ScheduleTaskEntity with resultJson and parametersJson.
     * When: DataArchiveRollbackJob.execute(JobExecutionContext) is called.
     * Then: The method should execute successfully and update the task status in the ScheduleTaskRepository.
     */
    @Test
    public void testExecuteSuccess() {
        // Given
        String jsonData = "{\"dataArchiveTaskId\":1,\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\",\"parametersJson\":{\"dataArchiveTaskId\":1,\"dataArchiveRollbackParameters\":{\"dataArchiveTaskId\":1}}," +
                "\"resultJson\":[{\"id\":1001,\"status\":\"PREPARING\",\"sourceDatabaseId\":1,\"targetDatabaseId\":2,\"jobType\":\"ROLLBACK\",\"resultJson\":{},\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\"}]," +
                "\"id\":1}";
        ScheduleTaskEntity scheduleTaskEntity = JsonUtils.fromJson(jsonData, ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(scheduleTaskEntity.getParametersJson(),
                DataArchiveRollbackParameters.class);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext() {});

        // Then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(any(Long.class), any(TaskStatus.class));
    }

    /**
     * Test method for execute(JobExecutionContext).
     * Given: A invalid DataArchiveRollbackParameters or ScheduleTaskEntity not found.
     * When: DataArchiveRollbackJob.execute(JobExecutionContext) is called.
     * Then: The method should not execute and update the task status in the ScheduleTaskRepository with FAILED status.
     */
    @Test
    public void testExecuteFailure() {
        // Given
        String jsonData = "{\"dataArchiveTaskId\":1,\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\",\"parametersJson\":{},\"id\":1}";
        ScheduleTaskEntity scheduleTaskEntity = JsonUtils.fromJson(jsonData, ScheduleTaskEntity.class);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveRollbackJob.execute(new JobExecutionContext() {}));
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(any(Long.class), TaskStatus.FAILED);
    }

    /**
     * Test method for execute(JobExecutionContext).
     * Given: A valid DataArchiveRollbackParameters and ScheduleTaskEntity with resultJson and parametersJson.
     * When: DataArchiveRollbackJob.execute(JobExecutionContext) is called with empty list of tasks.
     * Then: The method should not execute the tasks and update the task status in the ScheduleTaskRepository with DONE status.
     */
    @Test
    public void testExecuteEmptyTaskList() {
        // Given
        String jsonData = "{\"dataArchiveTaskId\":1,\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\",\"parametersJson\":{\"dataArchiveTaskId\":1,\"dataArchiveRollbackParameters\":{}}," +
                "\"resultJson\":[],\"id\":1}";
        ScheduleTaskEntity scheduleTaskEntity = JsonUtils.fromJson(jsonData, ScheduleTaskEntity.class);

        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(scheduleTaskEntity.getParametersJson(),
                DataArchiveRollbackParameters.class);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext() {});

        // Then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(any(Long.class), TaskStatus.DONE);
    }

    /**
     * Test method for execute(JobExecutionContext).
     * Given: A valid DataArchiveRollbackParameters and ScheduleTaskEntity with resultJson and parametersJson.
     * When: DataArchiveRollbackJob.execute(JobExecutionContext) is called with a list of tasks.
     * Then: The method should execute the tasks and update the task status in the ScheduleTaskRepository based on the status of the tasks.
     */
    @Test
    public void testExecuteWithTaskList() {
        // Given
        DataArchiveRollbackParametersBuilder rollbackParametersBuilder = new DataArchiveRollbackParametersBuilder();
        DataArchiveRollbackParameters rollbackParameters = rollbackParametersBuilder
                .dataArchiveTaskId(1L)
                .build();

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJob");
        scheduleTaskEntity.setJobGroup("testGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        scheduleTaskEntity.setResultJson(Collections.singletonList(createDlmTask("1001", "PREPARING")));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));

        List<DlmTask> taskUnits = List.of(createDlmTask("1001", "DONE"), createDlmTask("1002", "FAILED"));

        when(scheduleTaskRepository.updateStatusById(any(Long.class), any(TaskStatus.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext() {});

        // Then
        Mockito.verify(scheduleTaskRepository, Mockito.times(2)).updateStatusById(any(Long.class), any(TaskStatus.class));
        TaskStatus expectedStatus = TaskStatus.values()[TaskStatus.values().length % 2 + 1]; // DONE or FAILED
        Mockito.verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(expectedStatus));
    }

    /**
     * Helper method to create a DlmTask instance.
     *
     * @param id The id of the task.
     * @param status The status of the task.
     * @return A DlmTask instance with the given id and status.
     */
    private DlmTask createDlmTask(String id, String status) {
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(Long.parseLong(id));
        dlmTask.setStatus(status);
        return dlmTask;
    }

    /**
     * Helper method to get the task status based on the status of the tasks in the list.
     *
     * @param taskUnits The list of tasks.
     * @return The task status.
     */
    private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        boolean allDone = true;

        for (DlmTask taskUnit : taskUnits) {
            if (TaskStatus.DONE.equals(taskUnit.getStatus())) {
                continue;
            }
            allDone = false;
            break;
        }

        return allDone ? TaskStatus.DONE : TaskStatus.FAILED;
    }
}
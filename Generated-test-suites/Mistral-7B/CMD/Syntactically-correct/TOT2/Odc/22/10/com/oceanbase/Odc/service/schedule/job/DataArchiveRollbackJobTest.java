import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * Test class for DataArchiveRollbackJob
 */
class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

    @Test
    void testExecuteWithValidParameters() {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        JobExecutionContext context = new JobExecutionContextImpl();
        context.setMergedJobDataMap(Mockito.mapOf("jobDataKey", taskEntity));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        DlmTask dlmTask2 = new DlmTask();
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        doReturn(Optional.of(taskEntity)).when(dataArchiveRollbackJob.scheduleTaskRepository).findById(any(Long.class));

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Assertions.assertAll(() -> Assertions.assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(dlmTasks)),
                () -> Assertions.assertEquals(TaskStatus.DONE, taskEntity.getStatus()));

    }

    @Test
    void testExecuteWithInvalidDataArchiveTaskId() {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(-1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        JobExecutionContext context = new JobExecutionContextImpl();
        context.setMergedJobDataMap(Mockito.mapOf("jobDataKey", taskEntity));

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Assertions.assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
        Assertions.assertEquals(1, dataArchiveRollbackJob.getTaskStatus(dlmTasks).size());
        Assertions.assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(dlmTasks).get(0).getStatus());

    }

    @Test
    void testExecuteWithNullDataArchiveTaskEntity() {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        JobExecutionContext context = new JobExecutionContextImpl();

        doReturn(Optional.empty()).when(dataArchiveRollbackJob.scheduleTaskRepository).findById(any(Long.class));

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Assertions.assertEquals(TaskStatus.FAILED, taskEntity.getStatus());

    }

    @Test
    void testExecuteWithFailedDataArchiveTask() {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.FAILED);
        dlmTasks.add(dlmTask);

        doReturn(Optional.of(taskEntity)).when(dataArchiveRollbackJob.scheduleTaskRepository).findById(any(Long.class));
        doReturn(dlmTasks).when(dataArchiveRollbackJob).executeTask(any(Long.class), any(List.class));

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Assertions.assertEquals(TaskStatus.FAILED, taskEntity.getStatus());

    }
}
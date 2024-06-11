import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoAnnotations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParametersTest;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * ClassName: DataArchiveRollbackJobTest
 * Description: Test class for DataArchiveRollbackJob
 * @Author tinker
 * @Date 2023/6/26 20:22
 */
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskFound_RollbackSuccess() {
        // Given
        String jobName = "testJob";
        String jobGroup = "testGroup";
        Long taskId = 1L;
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setJobName(jobName);
        taskEntity.setJobGroup(jobGroup);
        taskEntity.setId(taskId);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        Assertions.assertNotNull(dataArchiveRollbackJob.getJobThread());
        Assertions.assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound_RollbackFailed() {
        // Given
        String jobName = "testJob";
        String jobGroup = "testGroup";
        Long taskId = 1L;
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setJobName(jobName);
        taskEntity.setJobGroup(jobGroup);
        taskEntity.setId(taskId);
        when(scheduleTaskRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        Assertions.assertNull(dataArchiveRollbackJob.getJobThread());
        Assertions.assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus());
    }

    @Test
    public void testExecute_TaskUnitsPreparedAndExecuted_RollbackSuccess() {
        // Given
        String jobName = "testJob";
        String jobGroup = "testGroup";
        Long taskId = 1L;
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setJobName(jobName);
        taskEntity.setJobGroup(jobGroup);
        taskEntity.setId(taskId);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity))
                .thenReturn(Optional.empty());

        when(scheduleTaskRepository.saveAll(Mockito.any()))
                .thenReturn(Arrays.asList(taskEntity));

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        Assertions.assertNotNull(dataArchiveRollbackJob.getJobThread());
        Assertions.assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus());
    }

    @Test
    public void testExecute_TaskUnitsPreparationFailed_RollbackFailed() {
        // Given
        String jobName = "testJob";
        String jobGroup = "testGroup";
        Long taskId = 1L;
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setJobName(jobName);
        taskEntity.setJobGroup(jobGroup);
        taskEntity.setId(taskId);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.saveAll(Mockito.any()))
                .thenThrow(new RuntimeException());

        // When & Then
        Assertions.assertThrows(RuntimeException.class, () -> dataArchiveRollbackJob.execute(new JobExecutionContext()));
        Assertions.assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus());
    }

    @Test
    public void testGetTaskStatus_AllTasksDone_RollbackSuccess() {
        // Given
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        when(scheduleTaskRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new ScheduleTaskEntity()));

        // When
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        // Then
        Assertions.assertEquals(TaskStatus.DONE, taskStatus);
    }

    @Test
    public void testGetTaskStatus_TasksNotAllDone_RollbackFailed() {
        // Given
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        taskUnits.get(0).setStatus(TaskStatus.DONE);
        taskUnits.get(1).setStatus(TaskStatus.FAILED);

        // When
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        // Then
        Assertions.assertEquals(TaskStatus.FAILED, taskStatus);
    }
}
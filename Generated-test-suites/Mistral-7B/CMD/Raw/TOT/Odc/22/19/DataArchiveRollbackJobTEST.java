package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_ValidDataArchiveTask_RollbackTaskExecutedSuccessfully() {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        // When
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // Then
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound_RollbackTaskFailed() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRolliveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        // When
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // Then
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_PrepareTasks_RollbackTaskPreparedSuccessfully() throws Exception {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(dataArchiveRollbackJob.executeTask(anyLong(), anyList())).thenReturn(true);

        // When
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // Then
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }

    @Test
    public void testExecute_PrepareTasksFailed_RollbackTaskFailed() throws Exception {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(dataArchiveRollbackJob.executeTask(anyLong(), anyList())).thenReturn(false);

        // When
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // Then
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecute_JsonParsingException_RollbackTaskFailed() {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson("{invalid json}");

        // When
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // Then
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }
}
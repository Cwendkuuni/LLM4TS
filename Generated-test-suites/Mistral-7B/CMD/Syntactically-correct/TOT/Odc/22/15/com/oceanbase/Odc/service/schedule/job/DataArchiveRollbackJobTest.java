package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.*;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ObjectMapper objectMapper;
    private ScheduleTaskRepository scheduleTaskRepository;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob() {
            {
                setScheduleTaskRepository(scheduleTaskRepository);
            }
        };
    }

    @Test
    public void testExecute_WhenDataArchiveTaskNotPresent_ThenStatusShouldBeFailed() throws JobExecutionException {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // when
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // then
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_WhenDataArchiveTaskPresent_ThenStatusShouldBeDone() throws JobExecutionException {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(scheduleTaskOptional);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(1L);
        dlmTask.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById(scheduleTaskEntity.getId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        doNothing().when(dataArchiveRollbackJob).executeTask(eq(scheduleTaskEntity.getId()), eq(dlmTasks));

        // when
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // then
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.DONE));
    }

    @Test
    public void testExecute_WhenDataArchiveTaskPresentAndExecuteTaskFailed_ThenStatusShouldBeFailed() throws JobExecutionException {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(scheduleTaskOptional);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(1L);
        dlmTask.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask);
        when(scheduleTaskRepository.findById(scheduleTaskEntity.getId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        doThrow(RuntimeException.class).when(dataArchiveRollbackJob).executeTask(eq(scheduleTaskEntity.getId()), eq(dlmTasks));

        // when
        Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveRollbackJob.execute(mock(JobExecutionContext.class)));

        // then
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));
    }
}
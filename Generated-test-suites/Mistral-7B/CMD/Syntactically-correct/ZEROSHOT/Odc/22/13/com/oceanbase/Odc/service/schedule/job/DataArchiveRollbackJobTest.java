package com.oceanbase.odc.service.schedule.job;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
        // Given
        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson("{\"dataArchiveTaskId\":1}",
                DataArchiveRollbackParameters.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        verifyTaskStatusUpdated(TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        // Given
        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson("{\"dataArchiveTaskId\":1}",
                DataArchiveRollbackParameters.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        verifyTaskStatusUpdated(TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TasksExecutedSuccessfully() throws JobExecutionException {
        // Given
        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson("{\"dataArchiveTaskId\":1}",
                DataArchiveRollbackParameters.class);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());
        when(scheduleTaskRepository.executeTask(taskEntity.getId(), taskUnits))
                .thenReturn(true);

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        verifyTaskStatusUpdated(TaskStatus.DONE);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_TasksExecutionFailed() throws JobExecutionException {
        // Given
        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson("{\"dataArchiveTaskId\":1}",
                DataArchiveRollbackParameters.class);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());
        when(scheduleTaskRepository.executeTask(taskEntity.getId(), taskUnits))
                .thenThrow(new RuntimeException("Test exception"));

        // When
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        verifyTaskStatusUpdated(TaskStatus.FAILED);
    }

    private void verifyTaskStatusUpdated(TaskStatus expectedStatus) {
        assertThat(taskEntity.getStatus(), is(expectedStatus));
    }
}
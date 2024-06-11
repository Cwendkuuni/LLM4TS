package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
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
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: tinker
 * @Date: 2023/6/26 20:05
 * @Description: Test class for DataArchiveRollbackJob
 */
@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        JobExecutionContext context = mock(JobExecutionContext.class);

        try {
            dataArchiveRollbackJob.execute(context);
            assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
        } catch (JobExecutionException e) {
            // expected
        }
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);

        List<DlmTask> dlmTasks = Collections.singletonList(new DlmTask());

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(true);

        JobExecutionContext context = mock(JobExecutionContext.class);

        try {
            dataArchiveRollbackJob.execute(context);
            assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
        } catch (JobExecutionException e) {
            // expected
        }

        verify(scheduleTaskRepository).updateStatusById(dataArchiveTask.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecute_TaskUnitsPrepared() {
        List<DlmTask> dlmTasks = Collections.singletonList(new DlmTask());

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(true);

        JobExecutionContext context = mock(JobExecutionContext.class);

        try {
            dataArchiveRollbackJob.execute(context);
            assertEquals(TaskStatus.DONE, taskEntity.getStatus());
        } catch (JobExecutionException e) {
            // expected
        }

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
        verify(dataArchiveRollbackJob).executeTask(taskEntity.getId(), dlmTasks);
    }
}
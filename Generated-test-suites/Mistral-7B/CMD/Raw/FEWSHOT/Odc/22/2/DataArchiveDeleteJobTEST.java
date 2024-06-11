package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveDeleteJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("non-existing-task-id");

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskAlreadyFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");

        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntity.getId()).thenReturn("existing-task-id");
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(dataArchiveTaskOption);

        List<DlmTask> dlmTasks = new ArrayList<>();

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository).updateStatusById(eq("existing-task-id"), any(TaskStatus.class));
        verify(dataArchiveDeleteJob).executeTask(eq("existing-task-id"), eq(dlmTasks));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");

        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntity.getId()).thenReturn("existing-task-id");
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.FAILED);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(dataArchiveTaskOption);

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository, never()).updateStatusById(eq("existing-task-id"), any(TaskStatus.class));
    }

    @Test
    public void testExecute_TasksPreparedAndExecuted() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");

        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskEntity.getId()).thenReturn("existing-task-id");
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(dataArchiveTaskOption);

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId("task1-id");
        dlmTasks.add(dlmTask1);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId("task2-id");
        dlmTasks.add(dlmTask2);

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        TaskStatus expectedTaskStatus = TaskStatus.SUCCESS;

        dataArchiveDeleteJob.execute(null);

        verify(scheduleTaskRepository).updateStatusById(eq("existing-task-id"), eq(expectedTaskStatus));
        verify(dataArchiveDeleteJob).executeTask(eq("existing-task-id"), eq(dlmTasks));
    }

    @Test
    public void testExecute_NullContext() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");

        dataArchiveDeleteJob.execute(null);

        verifyNoMoreInteractions(scheduleTaskRepository);
    }
}
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters.DataArchiveClearParametersBuilder;
import com.oceanbase.odc.service.schedule.model.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;
import lombok.SneakyThrows;

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
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId("nonexistent-task-id").build();

        when(scheduleTaskRepository.findById("nonexistent-task-id")).thenReturn(Optional.empty());

        try {
            dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
        } catch (JobExecutionException e) {
            assertEquals(TaskStatus.FAILED, e.getResult().getJobDataMap().get("status"));
        }
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(dataArchiveTask.getId()).build();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        try {
            dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
        } catch (JobExecutionException e) {
            assertEquals(TaskStatus.FAILED, e.getResult().getJobDataMap().get("status"));
        }
    }

    @Test
    public void testExecute_DataArchiveTaskFinished() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(dataArchiveTask.getId()).build();
        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId("test-job-id-1");
        dlmTasks.add(dlmTask);

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.PREPARING));
        verify(scheduleTaskRepository).updateStatusById(eq(dataArchiveTask.getId()), any(TaskStatus.class));
    }

    @SneakyThrows
    @Test
    public void testExecute_TaskExecutionSuccess() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParametersBuilder()
                .withDataArchiveTaskId(dataArchiveTask.getId()).build();
        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId("test-job-id-1");
        dlmTasks.add(dlmTask);

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

        TaskStatus taskStatus = getTaskStatus(dlmTasks);
        verify(scheduleTaskRepository).updateStatusById(eq(dataArchiveTask.getId()), eq(taskStatus));
    }
}
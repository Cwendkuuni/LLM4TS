package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
        dataArchiveClearParameters.setDataArchiveTaskId("non-existent-id");

        when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        try {
            dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
        } catch (JobExecutionException e) {
            assertEquals("Data archive task not found,rollback task fast failed.", e.getMessage());
            return;
        }

        assertFalse("Task status should not be updated when data archive task is not found.",
                dataArchiveDeleteJob.isTaskStatusUpdated());
    }

    @Test
    public void testExecute_DataArchiveTaskAlreadyDone() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing-id");

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

        when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setStatus(TaskStatus.FAILED);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity))
                .thenReturn(Optional.empty());

        when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.FAILED)))
                .thenReturn(1);

        try {
            dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
        } catch (JobExecutionException e) {
            assertEquals("Data archive task do not finish,data archive task id = " + scheduleTaskEntity.getId(),
                    e.getMessage());
            return;
        }

        assertFalse("Task status should not be updated when data archive task is already done.",
                dataArchiveDeleteJob.isTaskStatusUpdated());
    }

    @Test
    public void testExecute_DataArchiveTaskSuccess() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing-id");

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

        List<DlmTask> dlmTasks = new ArrayList<>();

        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setStatus(TaskStatus.PREPARING);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(2L);
        dlmTask2.setStatus(TaskStatus.RUNNING);

        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity))
                .thenReturn(Optional.empty());

        when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.FAILED)))
                .thenReturn(0);

        when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.DONE)))
                .thenReturn(1);

        when(scheduleTaskRepository.saveAll(ArgumentMatchers.anyList()))
                .thenReturn(dlmTasks);

        try {
            dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
        } catch (JobExecutionException e) {
            //expected
        }

        assertTrue("Task status should be updated when data archive task is successful.",
                dataArchiveDeleteJob.isTaskStatusUpdated());
    }

    @Test
    public void testExecute_DataArchiveTaskNotFoundAndUpdateStatusFailed() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("non-existent-id");

        when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.FAILED)))
                .thenThrow(new RuntimeException());

        try {
            dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
        } catch (JobExecutionException e) {
            //expected
        }

        assertFalse("Task status should not be updated when data archive task is not found and update status failed.",
                dataArchiveDeleteJob.isTaskStatusUpdated());
    }

    @Test
    public void testExecute_NullContext() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing-id");

        try {
            dataArchiveDeleteJob.execute(null);
        } catch (JobExecutionException e) {
            assertEquals("Context is null.", e.getCause().getMessage());
            return;
        }

        assertFalse("Task status should not be updated when context is null.",
                dataArchiveDeleteJob.isTaskStatusUpdated());
    }

    @Test
    public void testIsTaskStatusUpdated_FalseAtStart() {
        assertFalse("Task status should be false initially.", dataArchiveDeleteJob.isTaskStatusUpdated());
    }

    @Test
    public void testIsTaskStatusUpdated_TrueWhenUpdated() {
        dataArchiveDeleteJob.setTaskStatusUpdated(true);
        assertTrue("Task status should be true when set.", dataArchiveDeleteJob.isTaskStatusUpdated());
    }
}
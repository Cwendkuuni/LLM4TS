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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("non-exists-data-archive-task-id");

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        JobExecutionContext context = mock(JobExecutionContext.class);

        try {
            dataArchiveRollbackJob.execute(context);
            assertNull(dataArchiveRollbackJob.getJobThread());
            assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus());
        } catch (JobExecutionException e) {
            log.warn("Expected exception was not thrown.");
        }
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("data-archive-task-id");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("data-archive-task-id");
        dataArchiveTask.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        dataArchiveTask.setResultJson("{\"tasks\":[]}");

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        JobExecutionContext context = mock(JobExecutionContext.class);

        List<DlmTask> tasks = new ArrayList<>();
        tasks.add(new DlmTask());

        when(dataArchiveRollbackJob.executeTask(dataArchiveTask.getId(), tasks)).thenReturn(true);

        try {
            dataArchiveRollbackJob.execute(context);
            assertNotNull(dataArchiveRollbackJob.getJobThread());
            assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus());
        } catch (JobExecutionException e) {
            log.warn("Expected exception was not thrown.");
        }
    }

    @Test
    public void testGetTaskStatus() {
        List<DlmTask> tasks = new ArrayList<>();
        tasks.add(new DlmTask());
        tasks.add(new DlmTask());

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(tasks);
        assertEquals(TaskStatus.DONE, taskStatus);

        tasks.get(0).setStatus(TaskStatus.FAILED);
        tasks.get(1).setStatus(TaskStatus.DONE);

        taskStatus = dataArchiveRollbackJob.getTaskStatus(tasks);
        assertEquals(TaskStatus.FAILED, taskStatus);

        tasks.get(0).setStatus(TaskStatus.PREPARING);
        tasks.get(1).setStatus(TaskStatus.PREPARING);

        taskStatus = dataArchiveRollbackJob.getTaskStatus(tasks);
        assertEquals(TaskStatus.PREPARING, taskStatus);
    }
}
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
        taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        taskEntity.setId(1L);
        taskEntity.setJobName("testJob");
        taskEntity.setJobGroup("testGroup");
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        try {
            dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));
            fail("Expected JobExecutionException");
        } catch (JobExecutionException e) {
            // Expected
        }

        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound_PreparingTasks() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson(JsonUtils.toJsonString(Arrays.asList(
                new DlmTask(), new DlmTask())));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        try {
            dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));
            assertEquals(TaskStatus.DONE, getTaskStatus(taskUnits));
        } catch (JobExecutionException e) {
            fail("Unexpected JobExecutionException");
        }

        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskFound_RollbackTasks() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson(JsonUtils.toJsonString(Arrays.asList(
                new DlmTask().setId(1L).setStatus(TaskStatus.PREPARING),
                new DlmTask().setId(2L).setStatus(TaskStatus.DONE)
        )));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));

        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        try {
            dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));
            assertEquals(TaskStatus.DONE, getTaskStatus(taskUnits));
        } catch (JobExecutionException e) {
            fail("Unexpected JobExecutionException");
        }

        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
    }

    private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        JsonNode jsonNode = objectMapper.valueToTree(taskUnits);
        return JsonUtils.fromJson(jsonNode.get(0).get("status").asText(), TaskStatus.class);
    }
}
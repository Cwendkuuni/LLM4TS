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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveRollbackParameters dataArchiveRollbackParameters;
    private ObjectMapper objectMapper = new ObjectMapper();
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        dataArchiveRollbackParameters = new DataArchiveRollbackParameters();
        dataArchiveRollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveRollbackParameters));
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        try {
            dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));
            assertFalse(true);
        } catch (JobExecutionException e) {
            assertEquals("Data archive task not found", e.getMessage());
        }

        verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask);
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        try {
            dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));
            assertNotNull(jobThread);
            assertEquals(TaskStatus.DONE, getTaskStatus(dlmTasks));
        } catch (JobExecutionException e) {
            assertFalse(true);
        }

        verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testGetTaskStatus() {
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setStatus(TaskStatus.DONE);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);
        assertEquals(TaskStatus.DONE, getTaskStatus(dlmTasks));
    }

    private TaskStatus getTaskStatus(List<DlmTask> dlmTasks) {
        return DataArchiveRollbackJob.getTaskStatus(dlmTasks);
    }
}
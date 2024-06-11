package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
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
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_Success() throws Exception {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("dataArchiveTaskId");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("taskEntityId");
        taskEntity.setJobName("jobName");
        taskEntity.setJobGroup("jobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        when(scheduleTaskRepository.findById("taskEntityId")).thenReturn(Optional.of(taskEntity));

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.executeTask(taskEntity.getId(), dlmTasks)).thenReturn(true);

        // when
        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveRollbackJob.execute(context);

        // then
        verify(scheduleTaskRepository).updateStatusById("taskEntityId", TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("dataArchiveTaskId");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("taskEntityId");
        taskEntity.setJobName("jobName");
        taskEntity.setJobGroup("jobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        when(scheduleTaskRepository.findById("taskEntityId")).thenReturn(Optional.empty());

        // when
        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveRollbackJob.execute(context);

        // then
        verify(scheduleTaskRepository).updateStatusById("taskEntityId", TaskStatus.FAILED);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_TaskRollbackFailed() throws Exception {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("dataArchiveTaskId");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("taskEntityId");
        taskEntity.setJobName("jobName");
        taskEntity.setJobGroup("jobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

        when(scheduleTaskRepository.findById("taskEntityId")).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.executeTask(taskEntity.getId(), dlmTasks)).thenReturn(false);

        // when and then
        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById("taskEntityId", TaskStatus.FAILED);
    }

    @Test
    public void testExecute_EmptyDataArchiveTasks() throws Exception {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("dataArchiveTaskId");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("taskEntityId");
        taskEntity.setJobName("jobName");
        taskEntity.setJobGroup("jobGroup");
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> dlmTasks = Collections.emptyList();

        when(scheduleTaskRepository.findById("taskEntityId")).thenReturn(Optional.of(taskEntity));
        when(scheduleTaskRepository.executeTask(taskEntity.getId(), dlmTasks)).thenReturn(true);

        // when
        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveRollbackJob.execute(context);

        // then
        verify(scheduleTaskRepository).updateStatusById("taskEntityId", TaskStatus.DONE);
    }
}
package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobExecutionWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ObjectMapper objectMapper;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        objectMapper = new ObjectMapper();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
        // given
        List<DlmTask> dlmTasks = new ArrayList<>();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setJobName("testJobName");
        dataArchiveTask.setJobGroup("testJobGroup");
        dataArchiveTask.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
        dataArchiveTask.setResultJson(objectMapper.writeValueAsString(dlmTasks));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        // when
        JobExecutionContext context = new MockitoJobExecutionWrapper(null);
        context.setJobDetail(mock(JobDetail.class));
        JobExecutionContext contextWithData = dataArchiveRollbackJob.execute(context);

        // then
        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), TaskStatus.DONE);
        assertEquals(contextWithData, context);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        // when
        JobExecutionContext context = new MockitoJobExecutionWrapper(null);

        // then
        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_TasksPrepared() throws JobExecutionException {
        // given
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(1L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.PREPARING);
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setJobName("testJobName");
        dataArchiveTask.setJobGroup("testJobGroup");
        dataArchiveTask.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
        dataArchiveTask.setResultJson(objectMapper.writeValueAsString(dlmTasks));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        // when
        JobExecutionContext context = new MockitoJobExecutionWrapper(null);
        JobExecutionContext contextWithData = dataArchiveRollbackJob.execute(context);

        // then
        verify(dlmTask, times(1)).setId(anyLong());
        verify(dlmTask, times(1)).setSourceDatabaseId(dlmTask.getTargetDatabaseId());
        verify(dlmTask, times(1)).setTargetDatabaseId(dlmTask.getSourceDatabaseId());
        verify(dlmTask, times(1)).setJobType(JobType.ROLLBACK);
        verify(dlmTask, times(1)).setStatus(TaskStatus.DONE);
        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), TaskStatus.DONE);
        assertEquals(contextWithData, context);
    }

    @Test
    public void testExecute_TasksPreparing() throws JobExecutionException {
        // given
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(1L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.PREPARING);
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setJobName("testJobName");
        dataArchiveTask.setJobGroup("testJobGroup");
        dataArchiveTask.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
        dataArchiveTask.setResultJson(objectMapper.writeValueAsString(dlmTasks));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        // when
        JobExecutionContext context = new MockitoJobExecutionWrapper(null);
        JobExecutionContext contextWithData = dataArchiveRollbackJob.execute(context);

        // then
        verify(dlmTask, times(1)).setId(anyLong());
        verify(dlmTask, times(1)).setSourceDatabaseId(dlmTask.getTargetDatabaseId());
        verify(dlmTask, times(1)).setTargetDatabaseId(dlmTask.getSourceDatabaseId());
        verify(dlmTask, times(1)).setJobType(JobType.ROLLBACK);
        verify(dlmTask, times(1)).setStatus(TaskStatus.DONE);
        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), TaskStatus.DONE);
        assertEquals(contextWithData, context);
    }

    @Test
    public void testGetTaskStatus() {
        // given
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setStatus(TaskStatus.DONE);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setStatus(TaskStatus.FAILED);
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        // when
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        // then
        assertEquals(TaskStatus.FAILED, taskStatus);
    }
}
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveClearParameters dataArchiveClearParameters = Mockito.mock(DataArchiveClearParameters.class);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

        verify(log).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId=" + anyLong());
        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskAlreadyFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = Mockito.mock(DataArchiveClearParameters.class);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

        verifyNoMoreInteractions(log);
        verify(scheduleTaskRepository).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() {
        DataArchiveClearParameters dataArchiveClearParameters = Mockito.mock(DataArchiveClearParameters.class);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.FAILED);

        dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

        verify(log).warn("Data archive task do not finish,data archive task id = " + anyLong());
        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_PrepareTasksForClear() {
        DataArchiveClearParameters dataArchiveClearParameters = Mockito.mock(DataArchiveClearParameters.class);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> dlmTasks = Arrays.asList(Mockito.mock(DlmTask.class), Mockito.mock(DlmTask.class));
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(dlmTasks));

        dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

        for (DlmTask dlmTask : dlmTasks) {
            verify(dlmTask).setId(anyLong());
            verify(dlmTask).setJobType(eq(JobType.DELETE));
            verify(dlmTask).setStatus(eq(TaskStatus.PREPARING));
        }

        verify(log, never()).warn(anyString());
        verify(scheduleTaskRepository).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecute_TaskStatus() {
        DataArchiveClearParameters dataArchiveClearParameters = Mockito.mock(DataArchiveClearParameters.class);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> dlmTasks = Arrays.asList(Mockito.mock(DlmTask.class), Mockito.mock(DlmTask.class));
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(dlmTasks));

        when(executeTask(anyLong(), any(List.class))).thenReturn(Collections.emptyList());

        TaskStatus taskStatus = dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

        assertEquals(TaskStatus.SUCCESS, taskStatus);
        verify(log, never()).warn(anyString());
        verify(scheduleTaskRepository).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}
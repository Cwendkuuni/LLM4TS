package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @BeforeEach
    public void setup() {
        // Initialize mocks
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        // Given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("non-existing-task-id");
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When & Then
        dataArchiveDeleteJob.execute(context);
        Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskAlreadyFinished() throws JobExecutionException {
        // Given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        // When
        dataArchiveDeleteJob.execute(context);

        // Then
        Mockito.verify(taskEntity).setStatus(TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
        // Given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.PENDING);

        // When & Then
        dataArchiveDeleteJob.execute(context);
        Mockito.verify(taskEntity, Mockito.never()).setStatus(TaskStatus.FAILED);
    }

    @Test
    public void testExecute_Success() throws JobExecutionException, JsonProcessingException {
        // Given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask);

        when(taskEntity.getResultJson()).thenReturn(new ObjectMapper().writeValueAsString(taskUnits));
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING))
                .thenReturn(true);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
                .thenReturn(false);

        // When
        dataArchiveDeleteJob.execute(context);

        // Then
        Mockito.verify(taskEntity).setStatus(TaskStatus.PREPARING);
        Mockito.verify(taskEntity, Mockito.times(2)).setStatus(TaskStatus.FAILED);
        Mockito.verify(scheduleTaskRepository, Mockito.times(2))
                .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);

        String json = JsonUtils.toJsonString(taskEntity.getResultJson());
        List<DlmTask> updatedTaskUnits = JsonUtils.fromJson(json, new TypeReference<List<DlmTask>>() {});
        assertEquals(1, updatedTaskUnits.size());
        Assertions.assertEquals(TaskStatus.FAILED, updatedTaskUnits.get(0).getStatus());
    }
}
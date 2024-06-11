package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.ScheduleTaskRepositoryImpl;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_Success() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParamsJson(JsonUtils.toJsonString(rollbackParameters));
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask());

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleTaskRepository.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE))
                .thenReturn(true);

        // When
        dataArchiveRollbackJob.execute(null);

        // Then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParamsJson(JsonUtils.toJsonString(rollbackParameters));
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When
        dataArchiveRollbackJob.execute(null);

        // Then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test(expected = Exception.class)
    public void testExecute_JsonParsingException() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParamsJson("{invalid json}");
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        // When & Then
        dataArchiveRollbackJob.execute(null);
        fail("Expected JsonParsingException was not thrown.");
    }

    @Test
    public void testExecute_TaskStatusUpdateFailure() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParamsJson(JsonUtils.toJsonString(rollbackParameters));
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask());

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleTaskRepository.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE))
                .thenReturn(false);

        // When
        dataArchiveRollbackJob.execute(null);

        // Then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }
}
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntityBuilder;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private ObjectMapper objectMapper;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTaskEntity;
    private List<DlmTask> taskUnits;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        scheduleTaskEntity = new ScheduleTaskEntityBuilder().build();
        dataArchiveTaskEntity = new ScheduleTaskEntityBuilder().build().withId(1L).build();
        taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
    }

    @Test
    public void testExecute_NoDataArchiveTaskFound_StatusUpdatedToFailed() {
        // Given
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveRollbackJob.execute(context);

        // Then
        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getLastStatusById(1L));
    }

    @Test
    public void testExecute_DataArchiveTaskFound_TasksPreparedAndStatusUpdated() {
        // Given
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTaskEntity));

        // When
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntityBuilder().build().withId(2L).build();
        JsonNode jsonNode = objectMapper.valueToTree(new DataArchiveRollbackParameters());
        taskEntity.setParametersJson(JsonUtils.toJsonString(jsonNode));
        when(scheduleTaskRepository.save(taskEntity)).thenReturn(taskEntity);
        JobExecutionContext context = mock(JobExecutionContext.class);
        context.setResult(taskEntity);
        dataArchiveRollbackJob.execute(context);

        // Then
        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(taskEntity.getParametersJson(),
                DataArchiveRollbackParameters.class);
        assertEquals(rollbackParameters.getDataArchiveTaskId(), dataArchiveTaskEntity.getId());
        assertEquals(TaskStatus.DONE, scheduleTaskRepository.getLastStatusById(2L));
    }

    @Test
    public void testExecute_TasksPreparedAndStatusUpdated() {
        // Given
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTaskEntity));

        // When
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntityBuilder().build().withId(2L).build();
        JsonNode jsonNode = objectMapper.valueToTree(new DataArchiveRollbackParameters());
        taskEntity.setParametersJson(JsonUtils.toJsonString(jsonNode));
        when(scheduleTaskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING)).thenReturn(true);
        JobExecutionContext context = mock(JobExecutionContext.class);
        context.setResult(taskEntity);

        dataArchiveRollbackJob.execute(context);

        // Then
        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(taskEntity.getParametersJson(),
                DataArchiveRollbackParameters.class);
        assertEquals(rollbackParameters.getDataArchiveTaskId(), dataArchiveTaskEntity.getId());
        assertEquals(TaskStatus.DONE, scheduleTaskRepository.getLastStatusById(2L));
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
    }

    @Test
    public void testExecute_TasksPreparedAndStatusUpdatedWithTasksRolledBack() {
        // Given
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTaskEntity));

        // When
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntityBuilder().build().withId(2L).build();
        JsonNode jsonNode = objectMapper.valueToTree(new DataArchiveRollbackParameters());
        taskEntity.setParametersJson(JsonUtils.toJsonString(jsonNode));
        when(scheduleTaskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING)).thenReturn(true);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.ROLLING_BACK))
                .thenReturn(true);
        JobExecutionContext context = mock(JobExecutionContext.class);
        context.setResult(taskEntity);

        dataArchiveRollbackJob.execute(context);

        // Then
        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(taskEntity.getParametersJson(),
                DataArchiveRollbackParameters.class);
        assertEquals(rollbackParameters.getDataArchiveTaskId(), dataArchiveTaskEntity.getId());
        assertEquals(TaskStatus.ROLLING_BACK, scheduleTaskRepository.getLastStatusById(2L));
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.ROLLING_BACK);
    }

    @Test
    public void testExecute_TasksNotPrepared_StatusUpdatedToPreparing() {
        // Given
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTaskEntity));

        // When
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntityBuilder().build().withId(2L).build();
        JsonNode jsonNode = objectMapper.valueToTree(new DataArchiveRollbackParameters());
        taskEntity.setParametersJson(JsonUtils.toJsonString(jsonNode));
        when(scheduleTaskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING)).thenReturn(true);
        JobExecutionContext context = mock(JobExecutionContext.class);
        context.setResult(taskEntity);

        dataArchiveRollbackJob.execute(context);

        // Then
        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(taskEntity.getParametersJson(),
                DataArchiveRollbackParameters.class);
        assertEquals(rollbackParameters.getDataArchiveTaskId(), dataArchiveTaskEntity.getId());
        assertEquals(TaskStatus.PREPARING, scheduleTaskRepository.getLastStatusById(2L));
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
    }

    @Test
    public void testExecute_TasksPreparedAndRolledBackFailed_StatusUpdatedToFailed() {
        // Given
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTaskEntity));

        // When
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntityBuilder().build().withId(2L).build();
        JsonNode jsonNode = objectMapper.valueToTree(new DataArchiveRollbackParameters());
        taskEntity.setParametersJson(JsonUtils.toJsonString(jsonNode));
        when(scheduleTaskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING)).thenReturn(true);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.ROLLING_BACK)).thenReturn(false);
        JobExecutionContext context = mock(JobExecutionContext.class);
        context.setResult(taskEntity);

        dataArchiveRollbackJob.execute(context);

        // Then
        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(taskEntity.getParametersJson(),
                DataArchiveRollbackParameters.class);
        assertEquals(rollbackParameters.getDataArchiveTaskId(), dataArchiveTaskEntity.getId());
        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getLastStatusById(2L));
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.ROLLING_BACK);
    }

    @Test
    public void testExecute_TasksPreparedAndRolledBackSuccess_StatusUpdatedToDone() {
        // Given
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTaskEntity));

        // When
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntityBuilder().build().withId(2L).build();
        JsonNode jsonNode = objectMapper.valueToTree(new DataArchiveRollbackParameters());
        taskEntity.setParametersJson(JsonUtils.toJsonString(jsonNode));
        when(scheduleTaskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING)).thenReturn(true);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.ROLLING_BACK)).thenReturn(true);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.DONE)).thenReturn(true);
        JobExecutionContext context = mock(JobExecutionContext.class);
        context.setResult(taskEntity);

        dataArchiveRollbackJob.execute(context);

        // Then
        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(taskEntity.getParametersJson(),
                DataArchiveRollbackParameters.class);
        assertEquals(rollbackParameters.getDataArchiveTaskId(), dataArchiveTaskEntity.getId());
        assertEquals(TaskStatus.DONE, scheduleTaskRepository.getLastStatusById(2L));
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.ROLLING_BACK);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testGetTaskStatus_AllTasksDone_StatusUpdatedToDone() {
        // Given
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTaskEntity));

        // When
        when(scheduleTaskRepository.findAllByJobNameAndJobGroupAndIdNot(dataArchiveTaskEntity.getJobName(),
                dataArchiveTaskEntity.getJobGroup(), 1L)).thenReturn(Arrays.asList(new ScheduleTaskEntity(),
                new ScheduleTaskEntity()));
        when(scheduleTaskRepository.updateStatusById(dataArchiveTaskEntity.getId(), TaskStatus.DONE)).thenReturn(true);

        // Then
        dataArchiveRollbackJob.getTaskStatus(taskUnits);

        // Then
        verify(scheduleTaskRepository).updateStatusById(dataArchiveTaskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testGetTaskStatus_TasksNotAllDone_StatusUpdatedToPreparing() {
        // Given
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTaskEntity));

        // When
        when(scheduleTaskRepository.findAllByJobNameAndJobGroupAndIdNot(dataArchiveTaskEntity.getJobName(),
                dataArchiveTaskEntity.getJobGroup(), 1L)).thenReturn(Arrays.asList(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.updateStatusById(dataArchiveTaskEntity.getId(), TaskStatus.PREPARING)).thenReturn(true);

        // Then
        dataArchiveRollbackJob.getTaskStatus(taskUnits);

        // Then
        verify(scheduleTaskRepository).updateStatusById(dataArchiveTaskEntity.getId(), TaskStatus.PREPARING);
    }
}
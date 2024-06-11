package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobDetailFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() throws Exception {
        // Given
        JobExecutionContext context = new MockitoJobDetailFactory().newJobExecutionContext();
        context.setMergedJobDataMap(new HashMap<>());
        context.setResult(new ScheduleTaskEntity());

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        // Given
        JobExecutionContext context = new MockitoJobDetailFactory().newJobExecutionContext();
        context.setMergedJobDataMap(new HashMap<>());
        context.setResult(new ScheduleTaskEntity());

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When & Then
        dataArchiveRollbackJob.execute(context);

        Mockito.verify(scheduleTaskRepository, Mockito.times(1))
                .updateStatusById(Mockito.anyLong(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_TaskUnitsForRollback() throws Exception {
        // Given
        JobExecutionContext context = new MockitoJobDetailFactory().newJobExecutionContext();
        context.setMergedJobDataMap(new HashMap<>());
        context.setResult(new ScheduleTaskEntity());

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        List<DlmTask> taskUnits = Arrays.asList(
                new DlmTask(),
                new DlmTask()
        );

        Mockito.when(scheduleTaskEntity.getResultJson())
                .thenReturn(new ObjectMapper().writeValueAsString(taskUnits).getBytes());

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        DlmTask taskUnit1 = taskUnits.get(0);
        DlmTask taskUnit2 = taskUnits.get(1);

        assertEquals(TaskStatus.DONE, taskUnit1.getStatus());
        assertEquals(TaskStatus.DONE, taskUnit2.getStatus());
    }

    @Test
    public void testExecute_EmptyTaskUnits() throws Exception {
        // Given
        JobExecutionContext context = new MockitoJobDetailFactory().newJobExecutionContext();
        context.setMergedJobDataMap(new HashMap<>());
        context.setResult(new ScheduleTaskEntity());

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        Mockito.when(scheduleTaskEntity.getResultJson())
                .thenReturn(new byte[0]);

        // When & Then
        dataArchiveRollbackJob.execute(context);

        assertFalse(scheduleTaskRepository.findById(scheduleTaskEntity.getId())
                .isPresent());
    }
}
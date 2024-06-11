package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Slf4j
public class DataArchiveRollbackJobTest {
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        context = Mockito.mock(JobExecutionContext.class);
        taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("123456");

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        try {
            dataArchiveRollbackJob.execute(context);
        } catch (JobExecutionException e) {
            log.warn(e.getMessage());
        }

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("123456");

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnits.add(taskUnit);

        Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        Mockito.when(dataArchiveTask.getParametersJson()).thenReturn("{}");
        Mockito.when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        try {
            dataArchiveRollbackJob.execute(context);
        } catch (JobExecutionException e) {
            log.warn(e.getMessage());
        }

        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testGetTaskStatus() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setStatus(TaskStatus.PREPARING);
        DlmTask taskUnit2 = new DlmTask();
        taskUnit2.setStatus(TaskStatus.DONE);
        taskUnits.add(taskUnit1);
        taskUnits.add(taskUnit2);

        TaskStatus expectedTaskStatus = TaskStatus.DONE;

        assertEquals(expectedTaskStatus, dataArchiveRollbackJob.getTaskStatus(taskUnits));
    }

    @Test
    public void testGetTaskStatus_AllTasksFailed() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setStatus(TaskStatus.FAILED);
        DlmTask taskUnit2 = new DlmTask();
        taskUnit2.setStatus(TaskStatus.FAILED);
        taskUnits.add(taskUnit1);
        taskUnits.add(taskUnit2);

        TaskStatus expectedTaskStatus = TaskStatus.FAILED;

        assertEquals(expectedTaskStatus, dataArchiveRollbackJob.getTaskStatus(taskUnits));
    }

    @Test
    public void testGetTaskStatus_AllTasksPreparing() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setStatus(TaskStatus.PREPARING);
        DlmTask taskUnit2 = new DlmTask();
        taskUnit2.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit1);
        taskUnits.add(taskUnit2);

        assertFalse(dataArchiveRollbackJob.getTaskStatus(taskUnits).isDone());
    }

    @Test
    public void testGetTaskStatus_AllTasksPreparingOrFailed() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = new DlmTask();
        taskUnit1.setStatus(TaskStatus.PREPARING);
        DlmTask taskUnit2 = new DlmTask();
        taskUnit2.setStatus(TaskStatus.FAILED);
        taskUnits.add(taskUnit1);
        taskUnits.add(taskUnit2);

        assertFalse(dataArchiveRollbackJob.getTaskStatus(taskUnits).isDone());
    }
}
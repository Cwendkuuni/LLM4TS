package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("taskId");

        when(scheduleTaskRepository.findById("taskId")).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class, withSettings().lenient()));

        verify(scheduleTaskRepository, times(1)).updateStatusById("taskId", TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("taskId");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();

        when(scheduleTaskRepository.findById("taskId")).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getResultJson()).thenReturn("[\n" +
                "{\n" +
                "    \"id\": 1,\n" +
                "    \"sourceDatabaseId\": 1,\n" +
                "    \"targetDatabaseId\": 2,\n" +
                "    \"jobName\": \"jobName\",\n" +
                "    \"jobGroup\": \"jobGroup\",\n" +
                "    \"status\": \"PREPARING\"\n" +
                "},\n" +
                "{\n" +
                "    \"id\": 2,\n" +
                "    \"sourceDatabaseId\": 3,\n" +
                "    \"targetDatabaseId\": 3,\n" +
                "    \"jobName\": \"jobName\",\n" +
                "    \"jobGroup\": \"jobGroup\",\n" +
                "    \"status\": \"DONE\"\n" +
                "}]");

        DataArchiveRollbackParameters expectedRollbackParameters = new DataArchiveRollbackParameters();
        expectedRollbackParameters.setDataArchiveTaskId("taskId");

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class, withSettings().lenient()));

        verify(scheduleTaskRepository, times(1)).updateStatusById("taskId", TaskStatus.DONE);
    }

    @Test
    public void testExecute_TaskUnitsPrepared() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("taskId");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        DlmTask dlmTask2 = new DlmTask();

        when(scheduleTaskRepository.findById("taskId")).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getResultJson()).thenReturn("[\n" +
                "{\n" +
                "    \"id\": 1,\n" +
                "    \"sourceDatabaseId\": 1,\n" +
                "    \"targetDatabaseId\": 2,\n" +
                "    \"jobName\": \"jobName\",\n" +
                "    \"jobGroup\": \"jobGroup\",\n" +
                "    \"status\": \"PREPARING\"\n" +
                "},\n" +
                "{\n" +
                "    \"id\": 2,\n" +
                "    \"sourceDatabaseId\": 3,\n" +
                "    \"targetDatabaseId\": 3,\n" +
                "    \"jobName\": \"jobName\",\n" +
                "    \"jobGroup\": \"jobGroup\",\n" +
                "    \"status\": \"DONE\"\n" +
                "}]");

        DataArchiveRollbackParameters expectedRollbackParameters = new DataArchiveRollbackParameters();
        expectedRollbackParameters.setDataArchiveTaskId("taskId");

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class, withSettings().lenient()));

        verify(scheduleTaskRepository, times(1)).updateStatusById("taskId", TaskStatus.DONE);

        verify(dataArchiveRollbackJob, times(1)).executeTask("taskId", dlmTasks);
    }

    @Test
    public void testExecuteTask_TasksRolledBack() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("taskId");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        DlmTask dlmTask2 = new DlmTask();

        when(scheduleTaskRepository.findById("taskId")).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getResultJson()).thenReturn("[\n" +
                "{\n" +
                "    \"id\": 1,\n" +
                "    \"sourceDatabaseId\": 1,\n" +
                "    \"targetDatabaseId\": 2,\n" +
                "    \"jobName\": \"jobName\",\n" +
                "    \"jobGroup\": \"jobGroup\",\n" +
                "    \"status\": \"PREPARING\"\n" +
                "},\n" +
                "{\n" +
                "    \"id\": 2,\n" +
                "    \"sourceDatabaseId\": 3,\n" +
                "    \"targetDatabaseId\": 3,\n" +
                "    \"jobName\": \"jobName\",\n" +
                "    \"jobGroup\": \"jobGroup\",\n" +
                "    \"status\": \"DONE\"\n" +
                "}]");

        DataArchiveRollbackParameters expectedRollbackParameters = new DataArchiveRollbackParameters();
        expectedRollbackParameters.setDataArchiveTaskId("taskId");

        when(dataArchiveRollbackJob.executeTask("taskId", dlmTasks)).thenReturn(List.of(dlmTask1, dlmTask2));

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class, withSettings().lenient()));

        verify(scheduleTaskRepository, times(1)).updateStatusById("taskId", TaskStatus.DONE);
        verify(dataArchiveRollbackJob, times(1)).executeTask("taskId", dlmTasks);

        verify(dlmTask1, times(1)).setId(anyLong());
        verify(dlmTask1, times(1)).setSourceDatabaseId(anyLong());
        verify(dlmTask1, times(1)).setTargetDatabaseId(anyLong());
        verify(dlmTask1, times(1)).setJobType(JobType.ROLLBACK);
        verify(dlmTask1, times(1)).setStatus(TaskStatus.DONE);

        verify(dlmTask2, times(1)).setId(anyLong());
        verify(dlmTask2, times(1)).setSourceDatabaseId(anyLong());
        verify(dlmTask2, times(1)).setTargetDatabaseId(anyLong());
        verify(dlmTask2, times(1)).setJobType(JobType.ROLLBACK);
        verify(dlmTask2, times(1)).setStatus(TaskStatus.DONE);
    }

    @Test
    public void testGetTaskStatus_AllTasksDone() {
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        DlmTask dlmTask2 = new DlmTask();
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        TaskStatus expectedTaskStatus = TaskStatus.DONE;

        assertEquals(expectedTaskStatus, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
    }

    @Test
    public void testGetTaskStatus_TasksPreparing() {
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setStatus(TaskStatus.PREPARING);
        DlmTask dlmTask2 = new DlmTask();
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        TaskStatus expectedTaskStatus = TaskStatus.PREPARING;

        assertEquals(expectedTaskStatus, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
    }

    @Test
    public void testGetTaskStatus_TasksFailed() {
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setStatus(TaskStatus.FAILED);
        DlmTask dlmTask2 = new DlmTask();
        dlmTasks.add(dlmTask1);
        dlmTasks.add(dlmTask2);

        TaskStatus expectedTaskStatus = TaskStatus.FAILED;

        assertEquals(expectedTaskStatus, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
    }
}
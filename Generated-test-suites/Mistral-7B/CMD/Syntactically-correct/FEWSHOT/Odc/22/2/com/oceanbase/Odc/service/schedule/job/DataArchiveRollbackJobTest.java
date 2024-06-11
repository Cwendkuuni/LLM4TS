package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void init() {
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob() {
            {
                this.scheduleTaskRepository = scheduleTaskRepository;
            }
        };
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("dataArchiveTaskId");

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(null);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("dataArchiveTaskId");

        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(null);

        verify(scheduleTaskRepository, times(1))
                .updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testGetTaskStatus_AllTasksDone() {
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        taskUnits.forEach(taskUnit -> taskUnit.setStatus(TaskStatus.DONE));

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.DONE, taskStatus);
    }

    @Test
    public void testGetTaskStatus_SomeTasksNotDone() {
        List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
        taskUnits.get(0).setStatus(TaskStatus.DONE);
        taskUnits.get(1).setStatus(TaskStatus.PREPARING);

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.PREPARING, taskStatus);
    }
}
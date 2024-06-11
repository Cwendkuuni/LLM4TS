package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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
    private JsonUtils jsonUtils;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        jsonUtils = mock(JsonUtils.class);
        dataArchiveRollbackJob.setJsonUtils(jsonUtils);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(jsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class))).thenReturn(rollbackParameters);

        doReturn(Optional.empty()).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class).pliedTo(context -> taskEntity));

        verify(log).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId=1");
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
        ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(jsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class))).thenReturn(rollbackParameters);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        taskUnits.add(dlmTask);
        when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));

        doNothing().when(dataArchiveRollbackJob).executeTask(taskEntity.getId(), taskUnits);

        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class).pliedTo(context -> taskEntity));

        verify(log, never()).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId=1");
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), any(TaskStatus.class));
        verify(dataArchiveRollbackJob).executeTask(taskEntity.getId(), taskUnits.toArray(new DlmTask[0]));
    }
}
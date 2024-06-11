package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        when(context.getResult()).thenReturn(taskEntity);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setJobType(JobType.BACKUP);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);

        String taskJson = JsonUtils.toJson(taskUnits);
        dataArchiveTask.setResultJson(taskJson);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);

        when(scheduleTaskRepository.findById(2L)).thenReturn(dataArchiveTaskOption);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(eq(1L), any(TaskStatus.class));
    }
}
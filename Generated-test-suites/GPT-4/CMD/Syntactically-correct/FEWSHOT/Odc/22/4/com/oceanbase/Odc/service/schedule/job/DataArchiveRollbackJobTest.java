package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Test;
import org.junit.Assert;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setResult(new Object());
        taskEntity.setStatus(TaskStatus.PREPARING);
        
        DataArchiveRollbackParameters rollbackParams = new DataArchiveRollbackParameters();
        rollbackParams.setDataArchiveTaskId(2L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParams));

        ScheduleTaskRepository scheduleTaskRepositoryMock = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepositoryMock.findById(2L)).thenReturn(Optional.of(taskEntity));
        job.setScheduleTaskRepository(scheduleTaskRepositoryMock);
        
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(3L);
        dlmTask.setSourceDatabaseId(10L);
        dlmTask.setTargetDatabaseId(20L);
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(dlmTask);

        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);

        job.execute(context);
        Assert.assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
    }
}
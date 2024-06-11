package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        JobExecutionContext context = mock(JobExecutionContext.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setResultJson(JsonUtils.toJson(new ArrayList<DlmTask>()));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);

        when(context.getResult()).thenReturn(taskEntity);
        when(dataArchiveTaskOption.isPresent()).thenReturn(true);
        when(dataArchiveTaskOption.get()).thenReturn(dataArchiveTask);

        DataArchiveRollbackJob spy = org.mockito.Mockito.spy(dataArchiveRollbackJob);
        when(spy.scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);

        TaskStatus expectedTaskStatus = TaskStatus.DONE;

        dataArchiveRollbackJob.execute(context);
        
        assertEquals(expectedTaskStatus, dataArchiveTask.getStatus());
    }
}
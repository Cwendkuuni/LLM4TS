package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskEntity scheduleTaskEntity;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
    }

    @Test
    public void testExecute() {
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask1);
        
        when(JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        
        dataArchiveRollbackJob.execute(jobExecutionContext);
        
        assertNotNull(dataArchiveRollbackJob.jobThread);
    }

    @Test
    public void testExecuteWithNoDataArchiveTask() {
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(dataArchiveRollbackJob.scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());
        
        dataArchiveRollbackJob.execute(jobExecutionContext);
        
        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }
}
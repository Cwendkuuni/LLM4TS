package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

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
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        scheduleTaskEntity.setParametersJson(parametersJson);
        
        // Mock repository behavior
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(scheduleTaskEntity));
        
        // Set the mock repository
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        assertEquals(Thread.currentThread(), dataArchiveRollbackJob.getJobThread());
        assertNotNull(scheduleTaskEntity);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        scheduleTaskEntity.setParametersJson(parametersJson);
        
        // Mock repository behavior
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.empty());
        
        // Set the mock repository
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }
}
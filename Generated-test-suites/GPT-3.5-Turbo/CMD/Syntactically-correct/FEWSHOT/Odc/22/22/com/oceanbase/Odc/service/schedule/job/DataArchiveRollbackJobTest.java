package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {
    
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveRollbackParameters rollbackParameters;
    private ScheduleTaskEntity dataArchiveTask;
    
    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        
        context = mock(JobExecutionContext.class);
        scheduleTaskEntity = new ScheduleTaskEntity();
        rollbackParameters = new DataArchiveRollbackParameters();
        dataArchiveTask = new ScheduleTaskEntity();
    }
    
    @Test
    public void testExecute() {
        // Test jobThread assignment
        dataArchiveRollbackJob.execute(context);
        assertNotNull(dataArchiveRollbackJob.getJobThread());
        
        // Test dataArchiveTask not found
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
        
        // Test dataArchiveTask found
        dataArchiveTask.setResultJson("[]");
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, never()).updateStatusById(any(), eq(TaskStatus.FAILED));
        
        // Test task preparation for rollback
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setStatus(TaskStatus.PREPARING);
        taskUnits.add(taskUnit);
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveRollbackJob.execute(context);
        
        assertEquals(dataArchiveTask.getId(), taskUnit.getId());
        assertEquals(taskUnit.getTargetDatabaseId(), new Long(1));
        assertEquals(taskUnit.getSourceDatabaseId(), new Long(2));
        assertEquals(taskUnit.getJobType(), JobType.ROLLBACK);
        assertEquals(taskUnit.getStatus(), TaskStatus.DONE);
        
        // Test scheduleTaskRepository update
        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository, times(3)).updateStatusById(any(), any());
    }
}
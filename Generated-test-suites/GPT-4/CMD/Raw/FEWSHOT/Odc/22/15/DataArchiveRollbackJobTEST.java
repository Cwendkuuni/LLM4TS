
package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    @Mock
    JobExecutionContext jobExecutionContext;
    @Mock
    ScheduleTaskEntity scheduleTaskEntity;
    @Mock
    DataArchiveRollbackParameters dataArchiveRollbackParameters;
    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteDataArchiveAbsent() {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("params");
        when(dataArchiveRollbackParameters.getDataArchiveTaskId()).thenReturn(1234L);
        when(scheduleTaskRepository.findById(1234L)).thenReturn(Optional.empty());
        
        dataArchiveRollbackJob.execute(jobExecutionContext);
        
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
    
    @Test
    public void testExecuteDataArchivePresent() {
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        DlmTask taskUnit = mock(DlmTask.class);
        ArrayList<DlmTask> taskUnits = new ArrayList<>(Arrays.asList(taskUnit));

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("params");
        when(dataArchiveRollbackParameters.getDataArchiveTaskId()).thenReturn(1234L);
        when(scheduleTaskRepository.findById(1234L)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn("tasks");
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);
        
        dataArchiveRollbackJob.execute(jobExecutionContext);
        
        verify(taskUnit).setJobType(JobType.ROLLBACK);
        verify(taskUnit).setStatus(TaskStatus.DONE);
        verify(taskUnit).setId(DlmJobIdUtil.generateHistoryJobId(taskEntity.getJobName(), taskEntity.getJobGroup(),
                taskEntity.getId(), taskUnits.size()));
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), getTaskStatus(taskUnits));
    }
}

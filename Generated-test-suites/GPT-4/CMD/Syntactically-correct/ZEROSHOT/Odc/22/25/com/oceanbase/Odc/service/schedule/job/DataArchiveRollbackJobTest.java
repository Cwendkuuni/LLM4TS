
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

public class DataArchiveRollbackJobTest {
    
    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    @Mock
    private DlmTask taskUnit;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testExecute() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("");
        when(taskEntity.getResultJson()).thenReturn("");
        when(taskEntity.getJobName()).thenReturn("jobName");
        when(taskEntity.getJobGroup()).thenReturn("jobGroup");
        when(taskEntity.getId()).thenReturn(1L);
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(taskUnit.getSourceDatabaseId()).thenReturn(100L);
        when(taskUnit.getTargetDatabaseId()).thenReturn(101L);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);
        
        List<DlmTask> taskUnits = Arrays.asList(taskUnit);
        
        doNothing().when(scheduleTaskRepository).updateStatusById(anyLong(), any(TaskStatus.class));

        dataArchiveRollbackJob.execute(context);

        verify(taskEntity, times(1)).getParametersJson();
        verify(taskEntity, times(1)).getResultJson();
        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}

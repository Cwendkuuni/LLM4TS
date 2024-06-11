package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.core.shared.constant.TaskStatus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext jobExecutionContext;
    
    @Mock
    private ScheduleTaskEntity taskEntity;
    
    @Mock
    private DlmTask dlmTask;

    @Test
    public void testExecute_WithValidDataArchiveTask() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = mock(DataArchiveRollbackParameters.class);
        when(taskEntity.getParametersJson()).thenReturn(rollbackParameters);
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));

        // When
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), any(TaskStatus.class));
    }

    @Test
    public void testExecute_WithInvalidDataArchiveTask() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = mock(DataArchiveRollbackParameters.class);
        when(taskEntity.getParametersJson()).thenReturn(rollbackParameters);
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

        // When
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
    }
}
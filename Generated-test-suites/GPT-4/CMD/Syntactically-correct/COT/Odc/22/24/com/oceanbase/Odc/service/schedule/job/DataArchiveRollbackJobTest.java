
package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.*;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest{
    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext mockContext;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    @Test
    public void testExecute() {
         // Given
        ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
       
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        // When
        dataArchiveRollbackJob.execute(mockContext);

        // Verify
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.any(), Mockito.any());
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() {
        // Given
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        // When
        dataArchiveRollbackJob.execute(mockContext);

        // Verify
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.any(), TaskStatus.FAILED);
    }
}

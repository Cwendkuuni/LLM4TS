
package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Optional;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {
    
    @Mock
    JobExecutionContext context;

    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = mock(DataArchiveRollbackParameters.class);
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(DlmJobIdUtil.generateJobId(JobType.ROLLBACK));
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn("{}");

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository).updateStatusById(anyLong(), any());
    }

    // Similar TestMethods would be done for edge cases and exceptions, once resolved.
}


package com.oceanbase.odc.service.schedule.job;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;


@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {
  
    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @Mock 
    JobExecutionContext context;

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    DlmTask dlmTask;

    @Test
    public void testExecute() {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(rollbackParameters.toString());
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity), Optional.empty());
        when(taskEntity.getResultJson()).thenReturn(Arrays.asList(dlmTask).toString());
        when(dlmTask.getStatus()).thenReturn(TaskStatus.DONE, TaskStatus.PREPARING);
        when(DlmJobIdUtil.generateHistoryJobId(any(), any(), any(),any())).thenReturn(1L);

        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(any(), any());

    }
}

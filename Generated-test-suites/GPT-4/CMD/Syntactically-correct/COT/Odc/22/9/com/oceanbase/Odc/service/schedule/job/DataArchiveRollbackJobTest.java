package com.oceanbase.odc.service.schedule.job;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {
    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleTaskEntity taskEntity;

    @Mock
    DataArchiveRollbackParameters dataArchiveRollbackParameters;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;

    @Test
    public void testExecuteTaskWithoutRollbackParamsAndDataArchiveTask() {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }

    /**
     * Similar way other tests could be written
     */
}

package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity;
    @Mock
    private DlmTask taskUnit;
    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(context.getResult()).thenReturn(null);
        dataArchiveRollbackJob.execute(context);
        verify(context, times(1)).getResult();
    }

    @Test
    public void testExecute_TaskFound() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{'dataArchiveTaskId':1}");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        List<DlmTask> taskUnits = Arrays.asList(taskUnit);
        when(dataArchiveTask.getResultJson()).thenReturn("[{'sourceDatabaseId':1, 'targetDatabaseId':2, 'status': 'PREPARING'}]");

        when(dlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt())).thenReturn(1L);
        when(taskUnit.getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveRollbackJob.execute(context);
        verify(context, times(1)).getResult();
        verify(taskUnit, times(1)).setJobType(JobType.ROLLBACK);
        verify(taskUnit, times(1)).setStatus(TaskStatus.DONE);
    }
}

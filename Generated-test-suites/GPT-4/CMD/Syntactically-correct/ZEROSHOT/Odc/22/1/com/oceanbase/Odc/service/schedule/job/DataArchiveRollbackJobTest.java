
package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity taskEntity;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private DlmTask dlmTask;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.scheduleTaskRepository = scheduleTaskRepository;
    }

    @Test
    public void testExecute() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": \"1\"}");
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(dlmTask);
        when(taskEntity.getResultJson()).thenReturn("[{\"sourceDatabaseId\": \"1\", \"targetDatabaseId\": \"2\", \"status\": \"" + TaskStatus.PREPARING + "\"}]");
        when(dlmTask.getSourceDatabaseId()).thenReturn("1");
        when(dlmTask.getTargetDatabaseId()).thenReturn("2");
        when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);
        when(dlmTask.getJobType()).thenReturn(JobType.ROLLBACK);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(2)).findById(any());
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
    }
}

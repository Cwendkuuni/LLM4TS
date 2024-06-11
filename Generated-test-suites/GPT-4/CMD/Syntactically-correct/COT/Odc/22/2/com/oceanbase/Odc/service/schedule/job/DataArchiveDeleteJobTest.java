
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.service.dlm.model.DlmTask;

public class DataArchiveDeleteJobTest {
    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Mock
    private DlmTask dlmTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("");
        when(scheduleTaskEntity.getResultJson()).thenReturn("");
    }

    @Test
    public void testExecute() {
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask);
        when(dlmTask.getId()).thenReturn("Task_1");
        when(dlmTask.getJobType()).thenReturn(JobType.DELETE);
        when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);
        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(jobExecutionContext, atLeast(1)).getResult();
        verify(scheduleTaskEntity, atLeast(1)).getParametersJson();
        verify(scheduleTaskEntity, atLeast(1)).getResultJson();
    }

    @Test
    public void testDataArchiveTaskNotFound() {
        when(dataArchiveDeleteJob.getScheduleTaskRepository().findById(any())).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(dataArchiveDeleteJob.getScheduleTaskRepository(), times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testDataArchiveTaskNotDone() {
        when(dataArchiveDeleteJob.getScheduleTaskRepository().findById(any())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.TODO);
        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(dataArchiveDeleteJob.getScheduleTaskRepository(), times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }
}

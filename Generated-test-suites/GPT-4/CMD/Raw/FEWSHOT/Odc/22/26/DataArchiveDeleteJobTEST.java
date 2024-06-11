package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity, dataArchiveTask;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Mock
    private DlmTask dlmTask;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() {
        doReturn(taskEntity).when(context).getResult();
        doReturn("").when(taskEntity).getId();
        doReturn(Optional.empty()).when(scheduleTaskRepository).findById(any(String.class));
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(any(String.class), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteDataArchiveTaskNotDone() {
        doReturn(taskEntity).when(context).getResult();
        doReturn(dataArchiveTask).when(dataArchiveTaskOption).get();
        doReturn(TaskStatus.RUNNING).when(dataArchiveTask).getStatus();
        doReturn("").when(taskEntity).getId();
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(any(String.class), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteDataArchiveTaskDone() {
        doReturn(taskEntity).when(context).getResult();
        doReturn(dataArchiveTask).when(dataArchiveTaskOption).get();
        doReturn(TaskStatus.DONE).when(dataArchiveTask).getStatus();
        doReturn(Arrays.asList(dlmTask)).when(dataArchiveTask).getResultJson();

        doReturn("").when(dlmTask).getId();
        doReturn(JobType.DELETE).when(dlmTask).getJobType();
        doReturn(TaskStatus.PREPARING).when(dlmTask).getStatus();

        dataArchiveDeleteJob.execute(context);
        verify(dlmTask).setId(any(String.class));
        assertEquals(JobType.DELETE, dlmTask.getJobtype());
        assertEquals(TaskStatus.PREPARING, dlmTask.getStatus());
        verify(scheduleTaskRepository).updateStatusById(any(String.class), any(TaskStatus.class));
    }
}
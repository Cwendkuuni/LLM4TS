package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.junit.Before;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob archiveDeleteJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        archiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecuteNotFoundTask() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("");
        archiveDeleteJob.execute(context);
        verify(context).setResult(any(Throwable.class));
    }

    @Test
    public void testExecuteTaskNotDone() {
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters clearParameters = new DataArchiveClearParameters();
        clearParameters.setDataArchiveTaskId(1L);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(clearParameters));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(taskEntity.getId()).thenReturn(1L);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DOING);
        archiveDeleteJob.execute(context);
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteTaskSuccess() {
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters clearParameters = new DataArchiveClearParameters();
        clearParameters.setDataArchiveTaskId(1L);
        DlmTask dlmTask = new DlmTask();
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(clearParameters));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        when(taskEntity.getId()).thenReturn(1L);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(Arrays.asList(dlmTask)));
        archiveDeleteJob.execute(context);
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());
    }
}
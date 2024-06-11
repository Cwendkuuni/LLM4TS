package com.oceanbase.odc.service.schedule.job;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext contextMock;
    private ScheduleTaskEntity taskMock;
    
    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        contextMock = mock(JobExecutionContext.class);
        taskMock = mock(ScheduleTaskEntity.class);
    }

    @Test
    public void testExecuteWhenArchiveTaskNotFound() {
        when(contextMock.getResult()).thenReturn(taskMock);
        when(taskMock.getParametersJson()).thenReturn(JsonUtils.toJson(new DataArchiveClearParameters()));
        when(taskMock.getId()).thenReturn(1L);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(contextMock);
        verify(taskMock).setId(TaskStatus.FAILED);
    }
    
    @Test
    public void testExecuteWhenDataArchiveTaskNotDone() {
        when(contextMock.getResult()).thenReturn(taskMock);
        when(taskMock.getParametersJson()).thenReturn(JsonUtils.toJson(new DataArchiveClearParameters()));
        when(taskMock.getId()).thenReturn(1L);
        ScheduleTaskEntity mockArchiveTask = mock(ScheduleTaskEntity.class);
        when(mockArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(any())).thenReturn(Optional.of(mockArchiveTask));
        
        dataArchiveDeleteJob.execute(contextMock);
        verify(taskMock).setId(TaskStatus.FAILED);
    }
}
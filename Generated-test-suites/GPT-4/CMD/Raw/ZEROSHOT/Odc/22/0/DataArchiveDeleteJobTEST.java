
package com.oceanbase.odc.service.schedule.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.quartz.JobExecutionContext;

public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private ScheduleTaskEntity dataArchiveTask;

    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;

    @Mock
    private List<DlmTask> taskUnits;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_NoDataArchiveTask() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(),
                TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PROCESSING);

        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(),
                TaskStatus.FAILED);
    }

    @Test
    public void testExecute_Success() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(taskEntity.getResultJson()).thenReturn("[{}]");
        when(taskEntity.getId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }
}


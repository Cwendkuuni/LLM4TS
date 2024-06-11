package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext context;
    @Mock
    private ScheduleTaskEntity entity;

    @Before
    public void setup() {
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId(1);

        when(context.getResult()).thenReturn(entity);
        when(entity.getParametersJson()).thenReturn(JsonUtils.toJson(params));
    }

    @Test
    public void testExecuteTaskNotFound() {
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(entity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteTaskNotDone() {
        ScheduleTaskEntity task = new ScheduleTaskEntity();
        task.setStatus(TaskStatus.PREPARING);

        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.of(task));

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(entity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteValidTask() {
        ScheduleTaskEntity task = new ScheduleTaskEntity();
        task.setStatus(TaskStatus.DONE);

        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.of(task));

        when(entity.getResultJson()).thenReturn(JsonUtils.toJson(Arrays.asList(new DlmTask())));
        
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(entity.getId(), TaskStatus.PREPARING);
    }
}

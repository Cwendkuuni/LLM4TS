package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() throws Exception {

       // Perform initialization for mock objects.
       MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() throws Exception {

       Thread thread = Thread.currentThread();
       ScheduleTaskEntity mockEntity = new ScheduleTaskEntity();
       DataArchiveClearParameters parameters = new DataArchiveClearParameters();
       // Add set up for parameter fields here
       mockEntity.setParametersJson(JsonUtils.toJson(parameters));
       when(context.getResult()).thenReturn(mockEntity);

       // Add set up for scenario where data archive task is not present
       when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

       dataArchiveDeleteJob.execute(context);
       // Verify the task failed if data archive task was not found
       verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));

       // Clean-up
       reset(context);

       // Add set up for scenario where data archive task is present but not done
       when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(mockEntity));
       mockEntity.setStatus(TaskStatus.RUNNING);

       dataArchiveDeleteJob.execute(context);
       // Verify the task failed if data archive task was not done
       verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
       
       // Clean-up
       reset(context);

       // Add set up for scenario where data archive task is present and done
       mockEntity.setStatus(TaskStatus.DONE);

       dataArchiveDeleteJob.execute(context);
       // Verify the task status update based on task status
       verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), any());
    }
}

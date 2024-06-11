package com.oceanbase.odc.service.schedule.job;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    // Add more mocks for the rest of services and repositories

    @Before
    public void setUp() {
        // Initialize mocks
    }

    @Test
    public void testExecute_whenDataArchiveTaskMissing() {
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotDone() {
        // Similar setup to the above test

        // However, instead of returning Optional.empty(), return a valid but not done task 

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_whenDataArchiveTaskDone() {
        // Similar setup to the above test

        // However, return a valid and done task

        // Then verify the expected behaviors
    }
}
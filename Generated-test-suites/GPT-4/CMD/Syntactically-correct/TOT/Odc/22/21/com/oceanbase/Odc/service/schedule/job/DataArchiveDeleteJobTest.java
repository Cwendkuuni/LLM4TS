package com.oceanbase.odc.service.schedule.job;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {
    private DataArchiveDeleteJob job;
    private JobExecutionContext context;
    private ScheduleTaskRepository taskRepository;

    @Before
    public void setUp() {
        taskRepository = mock(ScheduleTaskRepository.class);
        context = mock(JobExecutionContext.class);
        job = new DataArchiveDeleteJob(taskRepository);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Set up context and taskEntity mocks
        // ...
        
        job.execute(context);
        
        // Verify task status update, logging, etc.
    }
    
    // Write additional tests for other scenarios...
}
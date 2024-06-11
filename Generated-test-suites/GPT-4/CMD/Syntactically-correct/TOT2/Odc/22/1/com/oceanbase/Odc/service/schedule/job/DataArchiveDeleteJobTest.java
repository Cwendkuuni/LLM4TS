package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

@RunWith(MockitoJUnitRunner.class)
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

    @Before
    public void setUp() {
        // Setup the mocks here
        //...
    }

    @Test
    public void testExecute() {   
        when(context.getResult()).thenReturn(taskEntity);
        
        // Add other mock behaviors and assertions...
        
        //Call the method to test
        dataArchiveDeleteJob.execute(context);
        
        // Add assertions to check the result
    }

    @Test
    public void testExecuteWithEmptyDataArchiveTaskOption() {
        // Test when dataArchiveTaskOption is not present
    }
    
    @Test
    public void testExecuteWithNotDoneDataArchiveTaskStatus() {
        // Test when dataArchiveTask status is not DONE 
    }
    
    // Add more test cases to cover edge cases and exception scenarios...
}
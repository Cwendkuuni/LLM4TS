package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

public class DataArchiveRollbackJobTest {
    
    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;
    
    @Mock
    JobExecutionContext context;
    
    @Mock
    ScheduleTaskEntity taskEntity;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testExecute() {
        // Stub setup here, and then:
        dataArchiveRollbackJob.execute(context);
        // Check interactions with mocked beans here.
    }
    
    @Test(expected = RuntimeException.class)
    public void testExecuteWithInvalidJsonParameters() {
        // Stub setup here with wrong data that will cause an exception, and then:
        dataArchiveRollbackJob.execute(context);
    }
    
    @Test(expected = RuntimeException.class)
    public void testExecuteWithInvalidJsonTaskUnits() {
        // Stub setup here with wrong data that will cause an exception, and then:
        dataArchiveRollbackJob.execute(context);
    }
    
    @Test
    public void testExecuteWithNotFoundTaskId() {
        // Stub setup here with taskId that not found in repository. Expected is without throwing an exception.
        dataArchiveRollbackJob.execute(context);
    }
}
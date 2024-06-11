package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

  @InjectMocks
  DataArchiveRollbackJob dataArchiveRollbackJob;

  @Mock
  JobExecutionContext jobExecutionContextMock;
  
  @Mock
  ScheduleTaskEntity scheduleTaskEntityMock;
  
  @Mock
  Optional<ScheduleTaskEntity> scheduleTaskEntityOptionalMock;

  @Before
  public void initMocks(){
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testExecute_jobThreadShouldBeCurrentThread() {
    
    dataArchiveRollbackJob.execute(jobExecutionContextMock);
    
    assertNotNull(dataArchiveRollbackJob.getJobThread());
    
    assertEquals(dataArchiveRollbackJob.getJobThread(), Thread.currentThread());
    
  }
  
  // Other mock and test cases go here.
 
}
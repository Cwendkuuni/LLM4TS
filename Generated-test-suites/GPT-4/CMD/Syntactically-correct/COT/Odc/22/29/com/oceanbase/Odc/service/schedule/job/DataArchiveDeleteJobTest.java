package com.oceanbase.odc.service.schedule.job;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DataArchiveDeleteJobTest {
  
  private DataArchiveDeleteJob dataArchiveDeleteJob;

  @Before
  public void setUp() {
    this.dataArchiveDeleteJob = new DataArchiveDeleteJob();
  }

  @Test(expected = NullPointerException.class)
  public void testExecuteWithNullContext() {
    this.dataArchiveDeleteJob.execute(null);
  }

  @Test
  public void testDataArchiveTaskNotFound() {
    // Mocking and setup of context and repository to simulate scenario
    JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
    Mockito.when(scheduleTaskRepository.findById(Mockito.any(UUID.class)))
        .thenReturn(Optional.empty());
    this.dataArchiveDeleteJob.execute(context);
    Mockito.verify(scheduleTaskRepository).updateStatusById(Mockito.any(UUID.class), TaskStatus.FAILED);
  }

  @Test
  public void testDataArchiveTaskNotDone() {
    // Mocking and setup of context and repository to simulate scenario
    JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
    ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
    scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
    Mockito.when(scheduleTaskRepository.findById(Mockito.any(UUID.class)))
        .thenReturn(Optional.of(scheduleTaskEntity));
    this.dataArchiveDeleteJob.execute(context);
    Mockito.verify(scheduleTaskRepository).updateStatusById(Mockito.any(UUID.class), TaskStatus.FAILED);
  }

  @Test
  public void testExecuteTaskSuccess() {
    // Mocking and setup of context and repository to simulate scenario
    JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
    // Setup necessary objects and repository methods for success scenario
    // Execute the method and assert/verify necessary outcomes according to the success scenario
  }
}
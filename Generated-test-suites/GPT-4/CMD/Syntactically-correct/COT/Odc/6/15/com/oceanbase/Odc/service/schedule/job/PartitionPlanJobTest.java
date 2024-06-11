package com.oceanbase.odc.service.schedule.job;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

  private PartitionPlanJob partitionPlanJob;

  @Before
  public void setUp() {
      partitionPlanJob = new PartitionPlanJob();
  }

  @Test
  public void testExecute_whenScheduleServiceReturnsNull_thenLogsWarning() {
      // TODO: Fill this in
  }

  @Test
  public void testExecute_whenDatabasePartitionPlanDisabled_thenScheduleServiceTerminateCalled() {
      // TODO: Fill this in
  }

  @Test(expected = RuntimeException.class)
  public void testExecute_whenTerminateThrowsSchedulerException_thenRuntimeExceptionIsThrown() {
      // TODO: Fill this in
  }

  @Test
  public void testExecute_whenPartitionPlanTaskServiceThrowsException_thenLogsWarning() {
      // TODO: Fill this in
  }

  @Test
  public void testExecute_whenSuccessful_thenPartitionPlanTaskServiceExecutePartitionPlanCalled() {
      // TODO: Fill this in
  }

  @Test(expected = UnExpectedException.class)
  public void testInterrupt_thenUnExpectedExceptionIsThrown() {
      partitionPlanJob.interrupt();
  }
}
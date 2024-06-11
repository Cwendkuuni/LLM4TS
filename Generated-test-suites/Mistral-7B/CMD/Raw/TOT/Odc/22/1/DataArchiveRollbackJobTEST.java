import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobRepository jobRepository;
 private ScheduleTaskEntity scheduleTaskEntity;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 jobRepository = Mockito.mock(JobRepository.class);
 scheduleTaskEntity = new ScheduleTaskEntity();
 }

 @Test
 public void testExecuteSuccess() throws Exception {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(new ObjectMapper().writeValueAsString(rollbackParameters));
 Mockito.when(jobRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 dataArchiveRollbackJob.execute(new JobExecutionContext());
 Mockito.verify(jobRepository, Mockito.times(1)).updateStatusById(1L, TaskStatus.DONE);
 }

 @Test
 public void testExecuteFailed() throws Exception {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(-1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(new ObjectMapper().writeValueAsString(rollbackParameters));
 Mockito.when(jobRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 try {
 dataArchiveRollbackJob.execute(new JobExecutionContext());
 fail("expected JobExecutionException");
 } catch (JobExecutionException e) {
 // expected
 }
 Mockito.verify(jobRepository, Mockito.times(1)).updateStatusById(1L, TaskStatus.FAILED);
 }

 @Test
 public void testExecuteNoDataArchiveTask() throws Exception {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(new ObjectMapper().writeValueAsString(rollbackParameters));
 Mockito.when(jobRepository.findById(1L)).thenReturn(Optional.empty());

 dataArchiveRollbackJob.execute(new JobExecutionContext());
 Mockito.verify(jobRepository, Mockito.times(1)).updateStatusById(1L, TaskStatus.FAILED);
 }
}
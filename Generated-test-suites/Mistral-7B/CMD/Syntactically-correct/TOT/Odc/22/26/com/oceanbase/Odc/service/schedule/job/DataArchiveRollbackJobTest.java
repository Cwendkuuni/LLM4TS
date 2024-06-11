import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobContextHolder;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobRepository jobRepository;
 private ObjectMapper objectMapper;
 private ScheduleTaskEntity taskEntity;
 private JobContextHolder jobContextHolder;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 jobRepository = mock(JobRepository.class);
 objectMapper = new ObjectMapper();
 taskEntity = new ScheduleTaskEntity();
 jobContextHolder = mock(JobContextHolder.class);

 when(jobRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity));
 when(jobContextHolder.getContext()).thenReturn(new JobExecutionContext());
 }

 @Test
 public void testExecute_ValidDataArchiveTask_RollbackAllTasks() throws Exception {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));

 when(jobRepository.updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.DONE))).thenReturn(1);

 dataArchiveRollbackJob.execute(null);

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 dlmTasks.add(dlmTask1);
 DataArchiveRollbackJob.executeTask(1L, dlmTasks);
 assertEquals(TaskStatus.DONE, getTaskStatus(dlmTasks));
 }

 @Test
 public void testExecute_InvalidDataArchiveTask_RollbackNoTasks() throws Exception {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));

 when(jobRepository.updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED))).thenReturn(1);

 dataArchiveRollbackJob.execute(null);
 assertFalse(DataArchiveRollbackJob.executeTask(1L, new ArrayList<>()).isEmpty());
 assertNotNull(getTaskStatus(new ArrayList<>()));
 assertEquals(TaskStatus.FAILED, getTaskStatus(new ArrayList<>()));
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_NullDataArchiveTask_RollbackNoTasks() throws Exception {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(null);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));

 dataArchiveRollbackJob.execute(null);
 }

 private JsonNode getTaskStatus(List<DlmTask> dlmTasks) {
 return JsonUtils.fromJson(objectMapper.writeValueAsString(dlmTasks), new TypeReference<JsonNode>() {});
 }
}
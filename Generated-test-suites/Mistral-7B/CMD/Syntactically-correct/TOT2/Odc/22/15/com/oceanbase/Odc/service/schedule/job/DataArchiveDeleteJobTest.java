import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private JobRepository jobRepository;

 @BeforeEach
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 jobRepository = mock(JobRepository.class);
 dataArchiveDeleteJob.setJobRepository(jobRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 ScheduleTaskEntity taskEntity = Mockito.spy(new ScheduleTaskEntity());
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("non-existent-task-id");

 when(jobRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(new JobExecutionContext() {});

 Mockito.verify(jobRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");
 taskEntity.setId("existing-task-id");
 taskEntity.setStatus(TaskStatus.CREATED);

 when(jobRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 dataArchiveDeleteJob.execute(new JobExecutionContext() {});

 Mockito.verify(jobRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() throws JobExecutionException {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");
 taskEntity.setId("existing-task-id");
 taskEntity.setStatus(TaskStatus.DONE);

 when(jobRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 ObjectMapper objectMapper = new ObjectMapper();
 List<DlmTask> expectedTasks = Arrays.asList(new DlmTask(), new DlmTask());
 String expectedResultJson = objectMapper.writeValueAsString(expectedTasks);

 when(jobRepository.findById(taskEntity.getId()))
 .thenReturn(Optional.of(taskEntity));
 when(jobRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING))
 .thenReturn(true);
 when(jobRepository.updateStatusById(taskEntity.getId(), TaskStatus.RUNNING))
 .thenReturn(true);
 when(jobRepository.updateStatusById(taskEntity.getId(), TaskStatus.COMPLETED))
 .thenReturn(true);
 when(jobRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
 .thenReturn(false);

 dataArchiveDeleteJob.execute(new JobExecutionContext() {});

 Assertions.assertEquals(TaskStatus.COMPLETED, getTaskStatus(Arrays.asList(expectedTasks)));
 }

 private TaskStatus getTaskStatus(List<DlmTask> dlmTasks) {
 return dlmTasks.stream()
 .map(DlmTask::getStatus)
 .findFirst()
 .orElse(TaskStatus.FAILED);
 }
}
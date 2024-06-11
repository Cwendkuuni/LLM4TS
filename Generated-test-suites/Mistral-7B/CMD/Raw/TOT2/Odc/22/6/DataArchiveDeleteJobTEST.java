import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private ObjectMapper objectMapper = mock(ObjectMapper.class);

 @SuppressWarnings("unchecked")
 @Test
 void testExecute_DataArchiveTaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(-1L);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.setJsonUtils(new JsonUtils());
 dataArchiveDeleteJob.setObjectMapper(objectMapper);

 Assertions.assertThrows(JobExecutionException.class, () ->
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class)));
 }

 @SuppressWarnings("unchecked")
 @Test
 void testExecute_DataArchiveTaskAlreadyFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.setJsonUtils(new JsonUtils());
 dataArchiveDeleteJob.setObjectMapper(objectMapper);

 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 Assertions.assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @SuppressWarnings("unchecked")
 @Test
 void testExecute_DataArchiveTaskNotFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.setJsonUtils(new JsonUtils());
 dataArchiveDeleteJob.setObjectMapper(objectMapper);

 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 Assertions.assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @SuppressWarnings("unchecked")
 @Test
 void testExecute_DataArchiveTasksSuccess() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

 when(objectMapper.convertValue(any(), any()))
 .thenReturn(dlmTasks);

 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.setJsonUtils(new JsonUtils());
 dataArchiveDeleteJob.setObjectMapper(objectMapper);

 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 Assertions.assertDoesNotThrow(() -> dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class)));
 }

 @Test
 void testExecute_NullContext() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.setJsonUtils(new JsonUtils());
 dataArchiveDeleteJob.setObjectMapper(objectMapper);

 Assertions.assertThrows(NullPointerException.class, () ->
 dataArchiveDeleteJob.execute(null));
 }
}
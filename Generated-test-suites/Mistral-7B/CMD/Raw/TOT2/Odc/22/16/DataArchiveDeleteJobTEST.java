import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private JobRepository jobRepository;
 private ObjectMapper objectMapper;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 jobRepository = mock(JobRepository.class);
 objectMapper = mock(ObjectMapper.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 dataArchiveDeleteJob = new DataArchiveDeleteJob() {
 @Override
 JobRepository getJobRepository() {
 return jobRepository;
 }

 @Override
 ObjectMapper getObjectMapper() {
 return objectMapper;
 }
 };
 }

 @Test
 public void testExecute_TaskNotFound() {
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 when(jobRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());
 dataArchiveDeleteJob.execute(context);
 verify(jobRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskNotFinished() {
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 when(jobRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
 dataArchiveDeleteJob.execute(context);
 verify(jobRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() throws JobExecutionException {
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 DlmTask dlmTask1 = new DlmTask();
 DlmTask dlmTask2 = new DlmTask();
 List<DlmTask> taskUnits = Arrays.asList(dlmTask1, dlmTask2);
 when(jobRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
 when(objectMapper.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
 dataArchiveDeleteJob.execute(context);
 verify(jobRepository).updateStatusById(taskEntity.getId(), TaskStatus.COMPLETED);
 }
}
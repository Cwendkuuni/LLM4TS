import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc. MetadbTestUtil;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJobTest;
import com.oceanbase.odc.service.schedule.JobContextTestUtil;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private JobRepository jobRepository;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 jobRepository = Mockito.mock(JobRepository.class);
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 DataArchiveDeleteJobTest.initMocks(dataArchiveDeleteJob, jobRepository, scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 String json = JsonUtils.toJsonString(dataArchiveClearParameters);
 taskEntity.setParametersJson(json);
 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // When
 JobExecutionContext context = JobContextTestUtil.createContext(taskEntity);
 try {
 dataArchiveDeleteJob.execute(context);
 } catch (JobExecutionException e) {
 // Then
 Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // Given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 String json = JsonUtils.toJsonString(dataArchiveClearParameters);
 taskEntity.setParametersJson(json);
 taskEntity.setStatus(TaskStatus.FAILED);
 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 // When
 JobExecutionContext context = JobContextTestUtil.createContext(taskEntity);
 try {
 dataArchiveDeleteJob.execute(context);
 } catch (JobExecutionException e) {
 // Then
 Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }
 }

 @Test
 public void testExecute_Success() throws Exception {
 // Given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 String json = JsonUtils.toJsonString(dataArchiveClearParameters);
 taskEntity.setParametersJson(json);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> tasks = new ArrayList<>();
 DlmTask task = new DlmTask();
 task.setId(1L);
 tasks.add(task);
 Mockito.when(jobRepository.saveAll(tasks))
 .thenReturn(tasks);

 // When
 JobExecutionContext context = JobContextTestUtil.createContext(taskEntity);
 dataArchiveDeleteJob.execute(context);

 // Then
 Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.SUCCESS);
 }
}
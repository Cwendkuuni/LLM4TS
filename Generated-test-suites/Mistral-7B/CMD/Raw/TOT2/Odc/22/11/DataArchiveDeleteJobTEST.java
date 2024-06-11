import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
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
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.AbstractDlmJob.JobThreadLocal;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

 private JobRepository scheduleTaskRepository = mock(JobRepository.class);
 private ObjectMapper jsonUtils = mock(ObjectMapper.class);

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob() {
 @Override
 JobRepository getScheduleTaskRepository() {
 return scheduleTaskRepository;
 }

 @Override
 ObjectMapper getJsonUtils() {
 return jsonUtils;
 }
 };

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("non-existing-id");

 when(scheduleTaskRepository.findById(ArgumentMatchers.eq("non-existing-id")))
 .thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(new JobExecutionContext());

 verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.eq(taskEntity.getId()),
 ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob() {
 @Override
 JobRepository getScheduleTaskRepository() {
 return scheduleTaskRepository;
 }

 @Override
 ObjectMapper getJsonUtils() {
 return jsonUtils;
 }
 };

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");
 taskEntity.setId("existing-id");
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setResultJson("[]");

 when(scheduleTaskRepository.findById(ArgumentMatchers.eq("existing-id")))
 .thenReturn(Optional.of(taskEntity));

 dataArchiveDeleteJob.execute(new JobExecutionContext());

 verify(scheduleTaskRepository, never()).updateStatusById(ArgumentMatchers.eq("existing-id"),
 ArgumentMatchers.not(ArgumentMatchers.eq(TaskStatus.FAILED)));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob() {
 @Override
 JobRepository getScheduleTaskRepository() {
 return scheduleTaskRepository;
 }

 @Override
 ObjectMapper getJsonUtils() {
 return jsonUtils;
 }
 };

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");
 taskEntity.setId("existing-id");

 when(scheduleTaskRepository.findById(ArgumentMatchers.eq("existing-id")))
 .thenReturn(Optional.of(taskEntity));

 dataArchiveDeleteJob.execute(new JobExecutionContext());

 verify(scheduleTaskRepository, never()).updateStatusById(ArgumentMatchers.eq("existing-id"),
 ArgumentMatchers.not(ArgumentMatchers.eq(TaskStatus.FAILED)));
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_InterruptedExecuteTask() {
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob() {
 @Override
 JobRepository getScheduleTaskRepository() {
 return scheduleTaskRepository;
 }

 @Override
 ObjectMapper getJsonUtils() {
 return jsonUtils;
 }
 };

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");
 taskEntity.setId("existing-id");
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setResultJson("[]");

 when(scheduleTaskRepository.findById(ArgumentMatchers.eq("existing-id")))
 .thenReturn(Optional.of(taskEntity));

 when(scheduleTaskRepository.updateStatusById(anyString(), ArgumentMatchers.not(ArgumentMatchers.eq(TaskStatus.FAILED))))
 .thenThrow(new JobExecutionException("Interrupted"));

 dataArchiveDeleteJob.execute(new JobExecutionContext());
 }

 @Test
 public void testExecute_Success() {
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob() {
 @Override
 JobRepository getScheduleTaskRepository() {
 return scheduleTaskRepository;
 }

 @Override
 ObjectMapper getJsonUtils() {
 return jsonUtils;
 }
 };

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");
 taskEntity.setId("existing-id");
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setResultJson("[]");

 when(scheduleTaskRepository.findById(ArgumentMatchers.eq("existing-id")))
 .thenReturn(Optional.of(taskEntity));

 when(jsonUtils.fromJson(ArgumentMatchers.anyString(), ArgumentMatchers.eq(List.class)))
 .then(invocation -> {
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId("history-task-id");
 return dlmTasks;
 });

 when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.eq("existing-id"), ArgumentMatchers.eq(TaskStatus.DONE)))
 .thenReturn(true);

 dataArchiveDeleteJob.execute(new JobExecutionContext());

 verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.eq("existing-id"),
 ArgumentMatchers.eq(TaskStatus.DONE));
 }
}
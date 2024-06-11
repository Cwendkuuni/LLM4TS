import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 scheduleTaskEntity = new ScheduleTaskEntity();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 try {
 dataArchiveDeleteJob.execute(getJobExecutionContext(dataArchiveClearParameters));
 } catch (JobExecutionException e) {
 // expected
 }

 Assert.assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getLastStatusById(dataArchiveClearParameters.getDataArchiveTaskId()));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId");
 scheduleTaskEntity.setId("taskId");
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 try {
 dataArchiveDeleteJob.execute(getJobExecutionContext(dataArchiveClearParameters));
 } catch (JobExecutionException e) {
 // expected
 }

 Assert.assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getLastStatusById(dataArchiveClearParameters.getDataArchiveTaskId()));
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId");
 scheduleTaskEntity.setId("taskId");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.PREPARING);
 dlmTasks.add(dlmTask);
 when(dataArchiveDeleteJob.executeTask(scheduleTaskEntity.getId(), dlmTasks))
 .thenReturn(true);

 try {
 dataArchiveDeleteJob.execute(getJobExecutionContext(dataArchiveClearParameters));
 } catch (JobExecutionException e) {
 // not expected
 Assert.fail();
 }

 Assert.assertEquals(TaskStatus.SUCCESS, scheduleTaskRepository.getLastStatusById(dataArchiveClearParameters.getDataArchiveTaskId()));
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_TaskUnitsExecutionFailed() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId");
 scheduleTaskEntity.setId("taskId");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId("taskId");
 dlmTask.setStatus(TaskStatus.FAILED);
 dlmTasks.add(dlmTask);
 when(dataArchiveDeleteJob.executeTask(scheduleTaskEntity.getId(), dlmTasks))
 .thenThrow(new RuntimeException());

 try {
 dataArchiveDeleteJob.execute(getJobExecutionContext(dataArchiveClearParameters));
 } catch (JobExecutionException e) {
 // expected
 }

 Assert.assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getLastStatusById(dataArchiveClearParameters.getDataArchiveTaskId()));
 }

 private JobExecutionContext getJobExecutionContext(DataArchiveClearParameters dataArchiveClearParameters) {
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(scheduleTaskEntity);
 when(context.getMergedJobDataMap().getString("parametersJson")).thenReturn(objectMapper.writeValueAsString(dataArchiveClearParameters));
 return context;
 }
}
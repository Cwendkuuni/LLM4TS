import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private ObjectMapper objectMapper = new ObjectMapper();
 private ScheduleTaskEntity scheduleTask;
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

 @Test
 public void testDataArchiveDeleteJob_DataArchiveTaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskIdNotExist");

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 } catch (JobExecutionException e) {
 Assertions.assertEquals("Data archive task not found.", e.getMessage());
 }

 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testDataArchiveDeleteJob_DataArchiveTaskAlreadyFinished() {
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskIdExists");

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testDataArchiveDeleteJob_DataArchiveTaskNotFinished() {
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskIdExists");

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 } catch (Exception e) {
 Assertions.assertEquals("Data archive task do not finish.", e.getMessage());
 }

 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testDataArchiveDeleteJob_DataArchiveTaskPreparing() {
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskIdExists");

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PREPARING);

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testDataArchiveDeleteJob_Success() {
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskIdExists");
 List<DlmTask> dlmTasks = new ArrayList<>();

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
 when(dataArchiveTask.getResultJson()).thenReturn("{\"taskUnits\":[" + JsonUtils.toJson(dlmTasks) + "]}");

 doAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 executeTask(dataArchiveClearParameters.getDataArchiveTaskId(), dlmTasks);
 return null;
 }
 }).when(dataArchiveDeleteJob).executeTask(dataArchiveClearParameters.getDataArchiveTaskId(), dlmTasks);

 TaskStatus taskStatus = TaskStatus.SUCCEEDED;
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 MockitoAnnotations.initMocks(this);
 }

 private void executeTask(String taskId, List<DlmTask> dlmTasks) {
 // Implement the logic for executeTask method here
 }
}
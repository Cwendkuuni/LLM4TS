import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId1");
 when(scheduleTaskRepository.findById("taskId1")).thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(createMockJobContext(taskEntity));

 assertEquals(TaskStatus.FAILED, dataArchiveClearParameters.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() throws JobExecutionException {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId1");
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.findById("taskId1")).thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

 dataArchiveDeleteJob.execute(createMockJobContext(taskEntity));

 assertEquals(TaskStatus.FAILED, dataArchiveClearParameters.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId1");
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.findById("taskId1")).thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

 dataArchiveDeleteJob.execute(createMockJobContext(taskEntity));

 assertEquals(TaskStatus.FAILED, dataArchiveClearParameters.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskPrepareSuccess() throws JobExecutionException {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId1");
 ScheduleTaskEntity dataArchiveTask = Mockito.mock(ScheduleTaskEntity.class);
 List<DlmTask> dlmTasks = Mockito.mock(List.class);
 when(scheduleTaskRepository.findById("taskId1")).thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
 when(dataArchiveTask.getResultJson()).thenReturn("[]");
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING)).thenReturn(true);
 when(scheduleTaskRepository.save(Mockito.any(ScheduleTaskEntity.class))).thenReturn(taskEntity);

 dataArchiveDeleteJob.execute(createMockJobContext(taskEntity));

 assertEquals(TaskStatus.PREPARING, dataArchiveClearParameters.getStatus());
 }

 private JobExecutionContext createMockJobContext(ScheduleTaskEntity taskEntity) {
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 when(context.getMergedJobDataMap().get("scheduleTaskEntity")).thenReturn(taskEntity);
 return context;
 }
}
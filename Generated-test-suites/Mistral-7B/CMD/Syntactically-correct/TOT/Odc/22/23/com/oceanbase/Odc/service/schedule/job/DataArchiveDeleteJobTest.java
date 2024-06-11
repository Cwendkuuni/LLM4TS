import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @BeforeEach
 public void setup() {
 ObjectMapper mapper = new ObjectMapper();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters clearParameters = new DataArchiveClearParameters();
 clearParameters.setDataArchiveTaskId("taskId1");

 when(scheduleTaskRepository.findById(clearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

 JobExecutionContext context = mock(JobExecutionContext.class);
 dataArchiveDeleteJob.execute(context);

 Assertions.assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getStatusById("taskId1"));
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters clearParameters = new DataArchiveClearParameters();
 clearParameters.setDataArchiveTaskId("taskId2");

 when(scheduleTaskRepository.findById(clearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

 JobExecutionContext context = mock(JobExecutionContext.class);
 dataArchiveDeleteJob.execute(context);

 Assertions.assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getStatusById("taskId2"));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters clearParameters = new DataArchiveClearParameters();
 clearParameters.setDataArchiveTaskId("taskId3");

 when(scheduleTaskRepository.findById(clearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);

 JobExecutionContext context = mock(JobExecutionContext.class);
 try {
 dataArchiveDeleteJob.execute(context);
 } catch (JobExecutionException e) {
 // expected
 }

 Assertions.assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getStatusById("taskId3"));
 }

 @Test
 public void testExecute_Success() {
 ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters clearParameters = new DataArchiveClearParameters();
 clearParameters.setDataArchiveTaskId("taskId4");
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.PREPARING);
 dlmTasks.add(dlmTask);

 when(scheduleTaskRepository.findById(clearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
 when(scheduleTaskEntity.getResultJson()).thenReturn("[]");

 JobExecutionContext context = mock(JobExecutionContext.class);
 dataArchiveDeleteJob.execute(context);

 Assertions.assertEquals(TaskStatus.SUCCESS, scheduleTaskRepository.getStatusById("taskId4"));
 }
}
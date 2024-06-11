import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveDeleteJobTestData;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveDeleteJobTest {

 private ObjectMapper objectMapper;
 @Mock
 private JobRepository scheduleTaskRepository;
 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setResultJson(objectMapper.writeValueAsString(List.of(new DlmTask())));
 dataArchiveTask.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.findAllByJobNameAndJobGroupAndStatusAndResultJsonNot(eq("id"), eq(1L),
 eq("status"), eq(TaskStatus.FAILED), anyString()))
 .thenReturn(List.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.SUCCESS);
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_Exception() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setResultJson(objectMapper.writeValueAsString(List.of(new DlmTask())));
 dataArchiveTask.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.findAllByJobNameAndJobGroupAndStatusAndResultJsonNot(eq("id"), eq(1L),
 eq("status"), eq(TaskStatus.FAILED), anyString()))
 .thenThrow(new RuntimeException());

 // when, then
 dataArchiveDeleteJob.execute(new JobExecutionContext() {});
 }

 @Test
 public void testGetTaskStatus() {
 // given
 List<DlmTask> dlmTasks = List.of(new DlmTask(), new DlmTask());

 // when
 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.SUCCESS, taskStatus);
 }

 @Test
 public void testGetTaskStatus_Exception() {
 // given
 List<DlmTask> dlmTasks = List.of(new DlmTask(), new DlmTask(), new DlmTask());

 // when, then
 assertThrows(RuntimeException.class, () -> dataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }

 @Test
 public void testGetTaskStatus_InProgress() {
 // given
 List<DlmTask> dlmTasks = List.of(new DlmTask(), new DlmTask());
 DlmTask dlmTask1 = dlmTasks.get(0);
 dlmTask1.setStatus(TaskStatus.IN_PROGRESS);

 // when
 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.IN_PROGRESS, taskStatus);
 }

 @Test
 public void testGetTaskStatus_Failed() {
 // given
 List<DlmTask> dlmTasks = List.of(new DlmTask(), new DlmTask());
 DlmTask dlmTask1 = dlmTasks.get(0);
 dlmTask1.setStatus(TaskStatus.FAILED);

 // when
 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.FAILED, taskStatus);
 }
}
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_TaskNotPresent() throws JobExecutionException {
 when(scheduleTaskRepository.findById(anyLong()))
 .thenReturn(Optional.empty());
 doReturn(TaskStatus.FAILED).when(dataArchiveDeleteJob).getTaskStatus(anyList());

 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(anyLong(), ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_TaskNotFinished() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 when(scheduleTaskRepository.findById(anyLong()))
 .thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskEntity.getStatus())
 .thenReturn(TaskStatus.RUNNING);

 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(anyLong(), ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_TaskFinished() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 when(scheduleTaskRepository.findById(anyLong()))
 .thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskEntity.getStatus())
 .thenReturn(TaskStatus.DONE);

 DlmTask dlmTask = mock(DlmTask.class);
 List<DlmTask> dlmTasks = Arrays.asList(dlmTask);

 when(JsonUtils.fromJson(anyString(), ArgumentMatchers.eq(DataArchiveClearParameters.class)))
 .thenReturn(dataArchiveClearParameters);
 when(JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

 doReturn(TaskStatus.PREPARING).when(dataArchiveDeleteJob).getTaskStatus(anyList());

 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 Mockito.verify(dataArchiveDeleteJob, Mockito.times(1)).executeTask(anyLong(), anyList());
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(anyLong(), ArgumentMatchers.eq(TaskStatus.PREPARING));
 }

 @Test
 public void testGetTaskStatus_TasksFailed() {
 List<DlmTask> dlmTasks = Arrays.asList(mock(DlmTask.class));
 dlmTasks.get(0).setStatus(TaskStatus.FAILED);

 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }

 @Test
 public void testGetTaskStatus_TasksRunning() {
 List<DlmTask> dlmTasks = Arrays.asList(mock(DlmTask.class));
 dlmTasks.get(0).setStatus(TaskStatus.RUNNING);

 assertEquals(TaskStatus.RUNNING, dataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }

 @Test
 public void testGetTaskStatus_TasksDone() {
 List<DlmTask> dlmTasks = Arrays.asList(mock(DlmTask.class));
 dlmTasks.get(0).setStatus(TaskStatus.DONE);

 assertEquals(TaskStatus.DONE, dataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }

 @Test
 public void testGetTaskStatus_EmptyTasks() {
 List<DlmTask> dlmTasks = Collections.emptyList();

 assertEquals(TaskStatus.NONE, dataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }
}
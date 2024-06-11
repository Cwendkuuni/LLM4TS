package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 private JobExecutionContext context;
 private ScheduleTaskEntity taskEntity;
 private DataArchiveClearParameters dataArchiveClearParameters;

 @Before
 public void setup() {
 ObjectMapper mapper = new ObjectMapper();
 context = mock(JobExecutionContext.class);
 taskEntity = new ScheduleTaskEntity();
 dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 when(context.getResult()).thenReturn(taskEntity);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getParametersJson()).thenReturn(mapper.writeValueAsString(dataArchiveClearParameters));
 }

 @Test
 public void testExecute_TaskNotFound() {
 dataArchiveClearParameters.setDataArchiveTaskId(2L);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());
 dataArchiveDeleteJob.execute(context);
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_TaskNotFinished() {
 taskEntity.setStatus(TaskStatus.RUNNING);
 dataArchiveDeleteJob.execute(context);
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_PrepareTasks() {
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getResultJson()).thenReturn(mapper.writeValueAsString(dlmTasks));
 dataArchiveDeleteJob.execute(context);
 assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_JobExecutionException() {
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getResultJson()).thenReturn(mapper.writeValueAsString(new DlmTask()));
 when(dlmJobRepository.executeTask(taskEntity.getId(), dlmTasks))
 .thenThrow(new RuntimeException("Job execution failed"));
 dataArchiveDeleteJob.execute(context);
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_JobSuccess() {
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getResultJson()).thenReturn(mapper.writeValueAsString(dlmTasks));
 when(dlmJobRepository.executeTask(taskEntity.getId(), dlmTasks))
 .thenReturn(true);
 dataArchiveDeleteJob.execute(context);
 assertEquals(TaskStatus.SUCCESS, taskEntity.getStatus());
 }
}
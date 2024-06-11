package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":1,\"dataArchiveClearParameters\":{\"dataArchiveTaskId\":1}}");
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // given
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(createContext(scheduleTaskEntity));

 // then
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() throws JobExecutionException {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(createContext(scheduleTaskEntity));

 // then
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(3L);
 dataArchiveTask.setStatus(TaskStatus.PREPARING);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.findById(3L)).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(createContext(scheduleTaskEntity));

 // then
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskSuccess() throws JobExecutionException {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(4L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.findById(4L)).thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.saveAll(dlmTasks)).thenReturn(dlmTasks);

 // when
 dataArchiveDeleteJob.execute(createContext(scheduleTaskEntity));

 // then
 assertEquals(TaskStatus.SUCCESS, scheduleTaskEntity.getStatus());
 }

 private JobExecutionContext createContext(ScheduleTaskEntity scheduleTaskEntity) {
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(scheduleTaskEntity);
 return context;
 }
}
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

 private ScheduleTaskEntity taskEntity;
 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;
 @Before
 public void setup() {
 taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");
 taskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveClearParameters()));
 }

 @Test
 public void testExecute_TaskNotFound() {
 when(scheduleTaskRepository.findById(taskEntity.getId())).thenReturn(Optional.empty());

 try {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 fail("Expected JobExecutionException");
 } catch (JobExecutionException e) {
 //pass
 }

 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskAlreadyDone() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 when(scheduleTaskRepository.findById(taskEntity.getId())).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(new ScheduleTaskEntity()));

 when(new ScheduleTaskEntity().getStatus()).thenReturn(TaskStatus.DONE);

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskNotDone() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 when(scheduleTaskRepository.findById(taskEntity.getId())).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(new ScheduleTaskEntity()));

 when(new ScheduleTaskEntity().getStatus()).thenReturn(TaskStatus.FAILED);

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute() throws Exception {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 when(scheduleTaskRepository.findById(taskEntity.getId())).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(new ScheduleTaskEntity()));

 when(new ScheduleTaskEntity().getStatus()).thenReturn(TaskStatus.DONE);

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTasks.add(dlmTask);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.saveAll(dlmTasks)).thenReturn(dlmTasks);

 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = objectMapper.writeValueAsString(dlmTasks);
 when(new ScheduleTaskEntity().getResultJson()).thenReturn(jsonString);

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.SUCCESS);
 }
}
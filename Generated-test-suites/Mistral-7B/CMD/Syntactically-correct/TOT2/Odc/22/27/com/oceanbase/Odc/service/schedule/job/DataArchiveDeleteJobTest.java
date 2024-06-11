package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.utils.DlmJobIdUtil;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 objectMapper = Mockito.spy(new ObjectMapper());
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 doReturn(Optional.empty()).when(scheduleTaskRepository).findById(ArgumentMatchers.eq(1L));

 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskInProgress() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setStatus(TaskStatus.IN_PROGRESS);
 dataArchiveTask.setParametersJson("{}");

 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(ArgumentMatchers.eq(1L));

 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskCompleted() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setParametersJson("{}");

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(ArgumentMatchers.eq(1L));
 doReturn(dlmTasks).when(scheduleTaskRepository).findAllByJobNameAndJobGroupAndStatus(ArgumentMatchers.eq("testJobName"),
 ArgumentMatchers.eq("testJobGroup"), ArgumentMatchers.eq(TaskStatus.PREPARING));

 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.FAILED));
 verify(objectMapper).writeValueAsString(dlmTasks);
 }
}
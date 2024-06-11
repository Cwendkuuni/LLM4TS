package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.PREPARING)).thenReturn(true);
 }

 @Test
 public void testExecute_TaskNotFound() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":2}");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getOne(1L).getStatus());
 }

 @Test
 public void testExecute_TaskNotFinished() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getOne(1L).getStatus());
 }

 @Test
 public void testExecute_Success() throws Exception {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTasks.add(dlmTask);

 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.PREPARING)).thenReturn(true);
 when(dataArchiveDeleteJob.executeTask(1L, dlmTasks)).thenReturn(new ArrayList<>());
 when(dataArchiveDeleteJob.getTaskStatus(dlmTasks)).thenReturn(TaskStatus.DONE);

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 assertEquals(TaskStatus.DONE, scheduleTaskRepository.getOne(1L).getStatus());
 }
}
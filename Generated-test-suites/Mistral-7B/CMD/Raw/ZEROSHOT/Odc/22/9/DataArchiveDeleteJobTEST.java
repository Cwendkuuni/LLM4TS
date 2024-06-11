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
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@SuppressWarnings("unchecked")
@Slf4j
public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_taskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 verify(scheduleTaskRepository, times(1))
 .updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_taskAlreadyDone() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // verify no tasks created and status updated to FAILED
 verify(scheduleTaskRepository, times(1))
 .updateStatusById(eq(1L), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_prepareTasks() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 DlmTask dlmTask2 = new DlmTask();
 dlmTasks.add(dlmTask1);
 dlmTasks.add(dlmTask2);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // verify tasks created with correct id and status
 verify(scheduleTaskRepository, times(1))
 .saveAll(argThat(dlmTasks -> dlmTasks.stream()
 .allMatch(dlmTask -> dlmTask.getId() != null && dlmTask.getJobType() == JobType.DELETE)));

 verify(scheduleTaskRepository, times(1))
 .updateStatusById(eq(1L), eq(TaskStatus.PREPARING));
 }
}
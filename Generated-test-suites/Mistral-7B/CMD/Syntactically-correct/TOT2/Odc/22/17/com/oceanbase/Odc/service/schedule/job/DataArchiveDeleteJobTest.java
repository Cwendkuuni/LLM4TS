package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.service.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(-1L);

 // when
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTask.getId());

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(dataArchiveTask.getId()), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.PENDING);
 dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTask.getId());

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 verify(scheduleTaskRepository, never()).updateStatusById(eq(dataArchiveTask.getId()), eq(TaskStatus.FAILED));
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_TaskUnitsEmpty() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTask.getId());
 List<DlmTask> taskUnits = new ArrayList<>();
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.getTaskUnitsByTaskId(dataArchiveTask.getId())).thenReturn(taskUnits);

 // when // throw exception
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_TaskUnitsNotEmpty() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTask.getId());
 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask());
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.getTaskUnitsByTaskId(dataArchiveTask.getId())).thenReturn(taskUnits);

 // when
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(dataArchiveTask.getId()), any(TaskStatus.class));
 }
}
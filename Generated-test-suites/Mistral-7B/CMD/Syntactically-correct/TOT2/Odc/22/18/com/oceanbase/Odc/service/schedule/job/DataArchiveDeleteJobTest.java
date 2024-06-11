package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

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
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("non-existing-id");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setParametersJson("{}");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.FAILED)))
 .thenReturn(1);

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
 scheduleTaskEntity.setParametersJson("{}");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.FAILED)))
 .thenReturn(1);

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTasksPreparationSuccess() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setParametersJson("{}");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.PREPARING)))
 .thenReturn(1);

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.PREPARING));
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_NullContext() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 fail("Expected a RuntimeException to be thrown");
 }
}
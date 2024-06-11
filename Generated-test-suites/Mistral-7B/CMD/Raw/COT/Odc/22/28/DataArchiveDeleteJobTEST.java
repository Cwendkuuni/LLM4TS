package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(null);

 verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.eq(dataArchiveClearParameters.getId()),
 EqualityComparators.equalTo(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setParametersJson("{}");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 dataArchiveDeleteJob.execute(null);

 verifyNoInteractions(scheduleTaskRepository);
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_DataArchiveTaskNotFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 dataArchiveTask.setParametersJson("{}");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 dataArchiveDeleteJob.execute(null);
 }

 @Test
 public void testExecute_DataArchiveTasksPreparedAndCleared() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setResultJson("[]");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask taskUnit1 = new DlmTask();
 taskUnit1.setId(1L);
 taskUnit1.setStatus(TaskStatus.PREPARING);
 taskUnits.add(taskUnit1);
 DlmTask taskUnit2 = new DlmTask();
 taskUnit2.setId(2L);
 taskUnit2.setStatus(TaskStatus.CLEARED);
 taskUnits.add(taskUnit2);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.saveAll(ArgumentMatchers.eq(taskUnits)))
 .thenReturn(taskUnits);

 dataArchiveDeleteJob.execute(null);

 verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.eq(dataArchiveClearParameters.getId()),
 EqualityComparators.notNullValue());
 }

}
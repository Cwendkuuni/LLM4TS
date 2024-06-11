package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.Arrays;
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

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(null);

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(ArgumentMatchers.eq(dataArchiveClearParameters.getDataArchiveTaskId()),
 ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

 dataArchiveDeleteJob.execute(null);

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(ArgumentMatchers.eq(dataArchiveClearParameters.getDataArchiveTaskId()),
 ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_Success() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

 dataArchiveDeleteJob.execute(null);

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(ArgumentMatchers.eq(dataArchiveClearParameters.getDataArchiveTaskId()),
 ArgumentMatchers.eq(TaskStatus.SUCCESS));
 }

}
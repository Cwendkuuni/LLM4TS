package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParametersTest;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = DataArchiveClearParametersTest.generateDataArchiveClearParameters();
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // When & Then
 dataArchiveDeleteJob.execute(null);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskFailed() throws JobExecutionException {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = DataArchiveClearParametersTest.generateDataArchiveClearParameters();
 ScheduleTaskEntity dataArchiveTask = DataArchiveClearParametersTest.generateScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.FAILED);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // When & Then
 dataArchiveDeleteJob.execute(null);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskDone() throws JobExecutionException {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = DataArchiveClearParametersTest.generateDataArchiveClearParameters();
 ScheduleTaskEntity dataArchiveTask = DataArchiveClearParametersTest.generateScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 DlmTask dlmTask2 = new DlmTask();
 dlmTasks.add(dlmTask1);
 dlmTasks.add(dlmTask2);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.saveAll(dlmTasks))
 .thenReturn(dlmTasks);

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.PREPARING));
 verify(scheduleTaskRepository, times(1)).saveAll(eq(dlmTasks.subList(0, 1)));
 verify(scheduleTaskRepository, times(1)).saveAll(eq(dlmTasks.subList(1, 2)));
 }
}
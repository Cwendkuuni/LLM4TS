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
import org.quartz.impl.JobDetail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveDeleteJob;

@SuppressWarnings("unchecked")
@Slf4j
public class DataArchiveDeleteJobTest {

 private ScheduleTaskEntity dataArchiveTask;
 private List<DlmTask> taskUnits;
 private JobExecutionContext context;
 private DataArchiveClearParameters dataArchiveClearParameters;
 private ScheduleTaskRepository scheduleTaskRepository;
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Mock
 private ScheduleTaskRepository mockScheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 context = new JobExecutionContext(new JobDetail("testJob", null, DataArchiveDeleteJob.class));
 context.setMergedJobDataMap(new JobDataMap());
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // given
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 List<DlmTask> dataArchiveTaskList = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dataArchiveTaskList.add(dlmTask);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.executeTask(dataArchiveTask.getId(), dataArchiveTaskList))
 .thenReturn(true);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), TaskStatus.SUCCESS);
 }

 @Test
 public void testGetTaskStatus() {
 // given
 List<DlmTask> dlmTaskList = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setStatus(TaskStatus.FAILED);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setStatus(TaskStatus.SUCCESS);
 dlmTaskList.add(dlmTask1);
 dlmTaskList.add(dlmTask2);

 // when
 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTaskList);

 // then
 assertEquals(TaskStatus.FAILED, taskStatus);
 }
}
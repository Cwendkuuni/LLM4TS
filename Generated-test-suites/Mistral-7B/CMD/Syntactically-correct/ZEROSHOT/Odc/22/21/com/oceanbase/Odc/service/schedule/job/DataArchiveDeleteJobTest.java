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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

@Slf4j
public class DataArchiveDeleteJobTest {

 private static final String JOB_NAME = "testJobName";
 private static final String JOB_GROUP = "testJobGroup";
 private static final Long DATA_ARCHIVE_TASK_ID = 1L;
 private static final String PARAMETERS_JSON = "{}";

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 ObjectMapper objectMapper = new ObjectMapper();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(DATA_ARCHIVE_TASK_ID);
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(1);
 when(scheduleTaskRepository.save(any(ScheduleTaskEntity.class))).thenReturn(new ScheduleTaskEntity());
 when(scheduleTaskRepository.findByResultJsonContaining(anyString())).thenReturn(new ArrayList<>());

 dataArchiveDeleteJob = new DataArchiveDeleteJob() {
 @Override
 ScheduleTaskRepository getScheduleTaskRepository() {
 return scheduleTaskRepository;
 }
 };
 }

 @Test
 public void testExecute_TaskNotPresent() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(DATA_ARCHIVE_TASK_ID);
 dataArchiveDeleteJob.execute(null);
 verify(scheduleTaskRepository, times(1)).updateStatusById(DATA_ARCHIVE_TASK_ID, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskAlreadyFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(DATA_ARCHIVE_TASK_ID);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));
 dataArchiveDeleteJob.execute(null);
 verify(scheduleTaskRepository, times(1)).updateStatusById(DATA_ARCHIVE_TASK_ID, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskIdNotFoundInScheduleTaskRepository() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(DATA_ARCHIVE_TASK_ID);
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.empty());
 dataArchiveDeleteJob.execute(null);
 verify(scheduleTaskRepository, times(1)).updateStatusById(DATA_ARCHIVE_TASK_ID, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskPreparing() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(DATA_ARCHIVE_TASK_ID);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.PREPARING);
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));
 dataArchiveDeleteJob.execute(null);
 verify(scheduleTaskRepository, times(1)).updateStatusById(DATA_ARCHIVE_TASK_ID, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskExecuteSuccess() throws Exception {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(DATA_ARCHIVE_TASK_ID);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 DlmTask dlmTask2 = new DlmTask();
 dlmTasks.add(dlmTask1);
 dlmTasks.add(dlmTask2);
 when(scheduleTaskRepository.findByResultJsonContaining(JsonUtils.toJson(dlmTasks))).thenReturn(dlmTasks);

 dataArchiveDeleteJob.execute(null);
 verify(scheduleTaskRepository, times(1)).updateStatusById(DATA_ARCHIVE_TASK_ID, TaskStatus.DONE);
 }

 @Test
 public void testGetTaskStatus() {
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setStatus(TaskStatus.DONE);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setStatus(TaskStatus.FAILED);
 dlmTasks.add(dlmTask1);
 dlmTasks.add(dlmTask2);

 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);
 assertEquals(TaskStatus.FAILED, taskStatus);
 }
}
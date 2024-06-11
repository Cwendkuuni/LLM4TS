package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_TaskStatusUpdatedToFailed() throws Exception {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(-1L);
 String taskEntityJson = JsonUtils.toJson(taskEntity);
 String rollbackParametersJson = JsonUtils.toJson(rollbackParameters);
 taskEntity.setParametersJson(taskEntityJson);
 taskEntity.setJobName("TestJobName");
 taskEntity.setJobGroup("TestJobGroup");
 taskEntity.setId(1L);

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // then
 verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskExists_TaskStatusUpdated() throws Exception {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setJobName("TestJobName");
 dataArchiveTask.setJobGroup("TestJobGroup");
 dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":1,\"resultJson\":[]}");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setJobName("TestJobName");
 taskEntity.setJobGroup("TestJobGroup");
 taskEntity.setParametersJson("{\"dataArchiveTaskId\":1,\"resultJson\":[{}]}");

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask taskUnit = new DlmTask();
 taskUnit.setId(1L);
 taskUnit.setSourceDatabaseId(1L);
 taskUnit.setTargetDatabaseId(2L);
 taskUnit.setStatus(TaskStatus.DONE);
 taskUnits.add(taskUnit);

 when(scheduleTaskRepository.getTaskUnitsByTaskId(1L)).thenReturn(taskUnits);

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // then
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_TaskUnitsExecutionFailed() throws Exception {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setJobName("TestJobName");
 dataArchiveTask.setJobGroup("TestJobGroup");
 dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":1,\"resultJson\":[{}]}");

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask taskUnit = new DlmTask();
 taskUnit.setId(1L);
 taskUnit.setSourceDatabaseId(1L);
 taskUnit.setTargetDatabaseId(2L);
 taskUnit.setStatus(TaskStatus.FAILED);
 taskUnits.add(taskUnit);

 when(scheduleTaskRepository.getTaskUnitsByTaskId(1L)).thenReturn(taskUnits);

 // when, then
 dataArchiveRollbackJob.execute(new JobExecutionContext());
 }
}
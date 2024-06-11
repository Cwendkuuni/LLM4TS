package com.oceanbase.odc.service.schedule.job;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @BeforeEach
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_ValidDataArchiveTask_StatusUpdatedToDone() {
 // given
 String jsonData = "{\"id\":1,\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\",\"parametersJson\":{\"dataArchiveTaskId\":1}}";
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJob");
 scheduleTaskEntity.setJobGroup("testGroup");
 scheduleTaskEntity.setParametersJson(jsonData);

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 // when
 dataArchiveRollbackJob.execute(null);

 // then
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
 }

 @Test
 public void testExecute_InvalidDataArchiveTask_StatusUpdatedToFailed() {
 // given
 String jsonData = "{\"id\":1,\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\",\"parametersJson\":{}}";
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJob");
 scheduleTaskEntity.setJobGroup("testGroup");
 scheduleTaskEntity.setParametersJson(jsonData);

 // when
 dataArchiveRollbackJob.execute(null);

 // then
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_StatusUpdatedToFailed() {
 // given
 String jsonData = "{\"id\":1,\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\",\"parametersJson\":{\"dataArchiveTaskId\":2}}";
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJob");
 scheduleTaskEntity.setJobGroup("testGroup");
 scheduleTaskEntity.setParametersJson(jsonData);

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(null);

 // then
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskExistsButRollbackFailed_StatusUpdatedToFailed() {
 // given
 String jsonData = "{\"id\":1,\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\",\"parametersJson\":{\"dataArchiveTaskId\":1}}";
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJob");
 scheduleTaskEntity.setJobGroup("testGroup");
 scheduleTaskEntity.setParametersJson(jsonData);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setResultJson("{\"taskUnits\":[]}");

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveRollbackJob.execute(null);

 // then
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskExistsButRollbackSucceeded_StatusUpdatedToDone() {
 // given
 String jsonData = "{\"id\":1,\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\",\"parametersJson\":{\"dataArchiveTaskId\":1}}";
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJob");
 scheduleTaskEntity.setJobGroup("testGroup");
 scheduleTaskEntity.setParametersJson(jsonData);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setResultJson("{\"taskUnits\":[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":1,\"status\":\"PREPARING\"}]}");

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setSourceDatabaseId(1L);
 dlmTask.setTargetDatabaseId(1L);
 dlmTask.setStatus(TaskStatus.DONE);
 taskUnits.add(dlmTask);
 dataArchiveRollbackJob.execute(null);

 // then
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
 }

 @AfterEach
 public void teardown() {
 dataArchiveRollbackJob = null;
 scheduleTaskRepository = null;
 }
}
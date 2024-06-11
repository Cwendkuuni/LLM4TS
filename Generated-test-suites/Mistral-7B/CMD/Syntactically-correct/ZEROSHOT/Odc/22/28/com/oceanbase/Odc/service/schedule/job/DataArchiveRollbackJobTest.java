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
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.JobExecutionContextImpl;
import com.oceanbase.tools.migrator.common.enums.JobType;

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
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("notExistDataArchiveTaskId");
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContextImpl(taskEntity));

 // then
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
 // given
 ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
 dataArchiveTaskEntity.setId("1");
 dataArchiveTaskEntity.setJobName("testJobName");
 dataArchiveTaskEntity.setJobGroup("testJobGroup");
 dataArchiveTaskEntity.setParametersJson("{}");

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("1");
 rollbackParameters.setDataArchiveTaskResultJson(objectMapper.writeValueAsString(getDlmTasks()));

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTaskEntity));

 List<DlmTask> dlmTasks = getDlmTasks();

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContextImpl(dataArchiveTaskEntity));

 // then
 assertEquals(TaskStatus.DONE, dataArchiveTaskEntity.getStatus());
 }

 private List<DlmTask> getDlmTasks() {
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId("1");
 dlmTask1.setSourceDatabaseId(1L);
 dlmTask1.setTargetDatabaseId(2L);
 dlmTask1.setJobType(JobType.ROLLBACK);
 dlmTask1.setStatus(TaskStatus.PREPARING);
 dlmTasks.add(dlmTask1);
 return dlmTasks;
 }
}
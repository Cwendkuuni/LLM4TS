package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
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
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ScheduleTaskEntity scheduleTaskEntity;

 @Before
 public void setUp() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
 scheduleTaskEntity = new ScheduleTaskEntity();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("nonExistentId");
 doReturn(Optional.empty()).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());
 dataArchiveRollbackJob.execute(null);
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("existingId");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTasks.add(dlmTask);
 doReturn(dlmTasks).when(dataArchiveRollbackJob).executeTask(scheduleTaskEntity.getId(), dlmTasks);
 TaskStatus taskStatus = TaskStatus.DONE;
 dataArchiveRollbackJob.execute(null);
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), taskStatus);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecute_NullContext() {
 dataArchiveRollbackJob.execute(null);
 }

}
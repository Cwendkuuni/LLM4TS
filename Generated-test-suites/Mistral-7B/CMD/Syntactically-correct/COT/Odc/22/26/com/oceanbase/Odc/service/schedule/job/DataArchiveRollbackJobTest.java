package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

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
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 }

 /**
 * Test for execute method when data archive task is present
 */
 @Test
 public void testExecute_DataArchiveTaskPresent() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 taskUnits.add(dlmTask);
 // when
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));
 dataArchiveRollbackJob.scheduleTaskRepository = scheduleTaskRepository;
 dataArchiveRollbackJob.execute(Mockito.argThat(context -> context.getResult().equals(taskEntity)));
 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
 }

 /**
 * Test for execute method when data archive task is not present
 */
 @Test
 public void testExecute_DataArchiveTaskNotPresent() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.empty();
 // when
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));
 dataArchiveRollbackJob.scheduleTaskRepository = scheduleTaskRepository;
 dataArchiveRollbackJob.execute(Mockito.argThat(context -> context.getResult().equals(taskEntity)));
 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }
}
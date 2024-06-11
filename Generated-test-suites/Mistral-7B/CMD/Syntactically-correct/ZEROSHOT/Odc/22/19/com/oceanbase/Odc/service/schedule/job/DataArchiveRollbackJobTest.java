import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
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
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.ModelMapper;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveRollbackJob;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ModelMapper modelMapper;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 modelMapper = mock(ModelMapper.class);
 when(modelMapper.toScheduleTaskEntity(any())).thenReturn(new ScheduleTaskEntity());
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveRollbackJob.setModelMapper(modelMapper);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 String taskId = "taskId1";
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(Long.parseLong("100001"));

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 // given
 String taskId = "taskId1";
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(taskId);
 scheduleTaskEntity.setName("taskName");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveRollbackParameters()));
 ScheduleTaskEntity dataArchiveTask = scheduleTaskEntity;

 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId(1L);
 dlmTask1.setSourceDatabaseId(1L);
 dlmTask1.setTargetDatabaseId(2L);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setId(2L);
 dlmTask2.setSourceDatabaseId(2L);
 dlmTask2.setTargetDatabaseId(1L);
 List<DlmTask> taskUnits = Arrays.asList(dlmTask1, dlmTask2);

 when(scheduleTaskRepository.findById(Long.parseLong(taskId))).thenReturn(Optional.of(dataArchiveTask));
 when(modelMapper.toScheduleTaskEntity(any())).thenReturn(dataArchiveTask);

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.DONE);
 }

 @Test(expected = IllegalStateException.class)
 public void testExecute_NullTaskEntities() {
 // given
 String taskId = "taskId1";
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(taskId);
 scheduleTaskEntity.setName("taskName");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(new DataArchiveRollbackParameters()));
 ScheduleTaskEntity dataArchiveTask = scheduleTaskEntity;

 // when
 when(scheduleTaskRepository.findById(Long.parseLong(taskId))).thenReturn(Optional.empty());

 // then
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});
 }

 @Test
 public void testGetTaskStatus() {
 // given
 List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());

 // when
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

 // then
 assertEquals(TaskStatus.PREPARING, taskStatus);

 // given
 for (DlmTask dlmTask : taskUnits) {
 dlmTask.setStatus(TaskStatus.DONE);
 }

 // when
 taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

 // then
 assertEquals(TaskStatus.DONE, taskStatus);
}
}
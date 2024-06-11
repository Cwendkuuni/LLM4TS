import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 Mockito.verify(scheduleTaskRepository).updateStatusById(ArgsConnecting.arg(Long.class), ArgsConnecting.arg(TaskStatus.class));
 Mockito.verify(scheduleTaskRepository, Mockito.never()).updateStatusById(ArgsConnecting.arg(Long.class), ArgsConnecting.arg(TaskStatus.DONE.name()));
 }

 @Test
 public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 List<DlmTask> dlmTasks = createDlmTasks();
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), ArgsConnecting.any())).thenAnswer(invocation -> invocation.getArgument(1));

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), ArgsConnecting.arg(TaskStatus.DONE.name()));
 }

 // helper methods
 private List<DlmTask> createDlmTasks() {
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);
 return dlmTasks;
 }
}
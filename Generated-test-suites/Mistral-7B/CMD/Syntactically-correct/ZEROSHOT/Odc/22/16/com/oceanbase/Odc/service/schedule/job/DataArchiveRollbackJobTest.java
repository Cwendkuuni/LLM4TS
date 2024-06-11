import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnit.class)
public class DataArchiveRollbackJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @InjectMocks
 private DataArchiveRollbackJob dataArchiveRollbackJob;

 @Rule
 public MockitoRule mockitoRule = MockitoJUnit.rule();

 private JobExecutionContext context;
 private ScheduleTaskEntity taskEntity;
 private DataArchiveRollbackParameters rollbackParameters;

 @Before
 public void setUp() {
 context = new JobExecutionContext();
 taskEntity = new ScheduleTaskEntity();
 rollbackParameters = new DataArchiveRollbackParameters();
 }

 // Test method to test execute method with valid input and expected output
 public void testExecuteWithValidInput() throws JobExecutionException {
 // Given
 doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());
 List<DlmTask> dlmTasks = Lists.newArrayList();

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.DONE));
 }

 // Test method to test execute method with invalid input and expected output
 public void testExecuteWithInvalidInput() {
 // Given
 doReturn(Optional.empty()).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());

 // When & Then
 dataArchiveRollbackJob.execute(context);
 verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
 }

 // Test method to test getTaskStatus method with valid input and expected output
 public void testGetTaskStatusWithValidInput() {
 // Given
 List<DlmTask> dlmTasks = Lists.newArrayList();
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
 doReturn(dlmTasks).when(dataArchiveRollbackJob).executeTask(anyLong(), anyList());

 // When
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

 // Then
 verify(dataArchiveRollbackJob).executeTask(anyLong(), anyList());
 verify(taskEntity).setStatus(eq(TaskStatus.DONE));
 assertEquals(TaskStatus.DONE, taskStatus);
 }

 // Test method to test getTaskStatus method with invalid input and expected output
 public void testGetTaskStatusWithInvalidInput() {
 // Given
 List<DlmTask> dlmTasks = Lists.newArrayList();
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

 // When & Then
 dataArchiveRollbackJob.getTaskStatus(dlmTasks);
 verify(scheduleTaskRepository, never()).findById(anyLong());
 verify(taskEntity, never()).setStatus(any());
 assertEquals(TaskStatus.UNKNOWN, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
 }
}